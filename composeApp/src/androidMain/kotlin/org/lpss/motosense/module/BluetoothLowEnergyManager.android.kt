package org.lpss.motosense.module

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import org.lpss.motosense.model.DeviceData
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.Result
import org.lpss.motosense.util.ResultError
import java.util.UUID


class AndroidBluetoothLowEnergyManager(
    private val context: Context
): BluetoothLowEnergyManager {

    private val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private val bluetoothDevices: MutableList<BluetoothDevice> = mutableListOf()
    private var bluetoothDevice: BluetoothDevice? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var scanCallback: ScanCallback? = null

    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun startScanning(
        onScanFinished: (Result<String>) -> Unit,
    ) {
        Log.d(TAG, "Starting BLE Scan, Manager: $bluetoothManager, Adapter: $bluetoothAdapter, Scanner: $bluetoothLeScanner")
        scanCallback = object : ScanCallback() {
            @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                bluetoothDevices.add(result.device)
                onScanFinished(Result.Success(result.device.name))
                Log.d(TAG, "Device found: " + result.device.getName() + " - " + result.device.getAddress())
            }

            override fun onScanFailed(errorCode: Int) {
                onScanFinished(Result.Error(ResultError.UnknownError("Scan failed with error code: $errorCode")))
                Log.e(TAG, "Scan failed with error: $errorCode")
            }
        }

        val filter = ScanFilter.Builder()
            .setDeviceName("Portenta-H7")
            .build()
        val filters = listOf(filter)

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        bluetoothLeScanner.startScan(filters, settings, scanCallback)
    }

    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun stopScanning() {
        scanCallback?.let { cb ->
            bluetoothLeScanner?.stopScan(cb)
            scanCallback = null
            Log.d(TAG, "BLE Scan stopped")
        } ?: run {
            Log.d(TAG, "stopScanning called but scanCallback was null")
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun reset() {
        bluetoothDevice = null
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun startDataReadings(
        deviceName: String,
        onDeviceDataReceived: (DeviceData) -> Unit,
    ) {
        bluetoothDevice = bluetoothDevices.find { it.name == deviceName }
        requireNotNull(bluetoothDevice) { "Bluetooth device is not connected" }

        val bluetoothGattCallback = object : BluetoothGattCallback() {
            @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG, "Connected to GATT server. Requesting MTU...")
                    val desiredMtu = 517
                    val requested = gatt.requestMtu(desiredMtu)
                    Log.d(TAG, "requestMtu($desiredMtu) initiated: $requested")
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BLE", "Disconnected from GATT server.")
                    gatt.close()
                }
            }

            @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
            override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
                Log.d(TAG, "onMtuChanged: mtu=$mtu status=$status")
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "MTU negotiated: $mtu — discovering services...")
                    gatt.discoverServices()
                } else {
                    Log.e(TAG, "MTU request failed (status=$status).")
                }
            }

            @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val service = gatt.getService(SERVICE_UUID)
                    val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)

                    if (characteristic != null) {
                        val success = gatt.setCharacteristicNotification(characteristic, true)
                        if (success) {
                            val descriptor = characteristic.getDescriptor(CLIENT_CHAR_CONFIG)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                            } else {
                                @Suppress("DEPRECATION")
                                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                @Suppress("DEPRECATION")
                                gatt.writeDescriptor(descriptor)
                            }
                        } else {
                            Log.e("BLE", "Failed to enable notifications")
                        }
                    } else {
                        Log.e("BLE", "Characteristic not found")
                    }
                } else {
                    Log.e("BLE", "Service discovery failed with status: $status")
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                runCatching {
                    Log.d(TAG, "fromByteArray: Parsing DeviceData from byte array: ${value.joinToString { it.toString() }}")
                    Log.d(TAG, "fromByteArray: byteArray.size=${value.size} bytes")
                    onDeviceDataReceived(
                        DeviceData.fromByteArray(value)
                    )
                }.onFailure {
                    Log.e(TAG, "Failed to parse DeviceData from byte array", it)
                }
            }
        }

        bluetoothGatt = bluetoothDevice!!.connectGatt(context, false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE)
        bluetoothGatt!!.connect()
    }

    @RequiresPermission(anyOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION])
    override fun stopDataReadings() {
        requireNotNull(bluetoothGatt) { "Bluetooth GATT is not connected" }
        bluetoothGatt!!.close()
    }

    companion object {
        private const val TAG = "BLE"
        private val SERVICE_UUID = UUID.fromString("19b10000-0001-0000-0000-000000000000")
        private val CHARACTERISTIC_UUID = UUID.fromString("19b10001-0001-0000-0000-000000000000")
        private val CLIENT_CHAR_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}
