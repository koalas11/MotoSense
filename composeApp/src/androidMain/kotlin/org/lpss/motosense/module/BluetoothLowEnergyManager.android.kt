package org.lpss.motosense.module

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
import org.lpss.motosense.repository.RepositoryError
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.Result
import java.util.UUID


class AndroidBluetoothLowEnergyManager(
    private val context: Context
): BluetoothLowEnergyManager {

    private val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private var bluetoothDevice: BluetoothDevice? = null
    private var bluetoothGatt: BluetoothGatt? = null

    override fun startScanning(
        onScanFinished: (Result<Unit>) -> Unit,
    ) {
        val scanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                bluetoothDevice = result.device
                onScanFinished(Result.Success(Unit))
                Log.d(TAG, "Device found: " + result.device.getName() + " - " + result.device.getAddress())
            }

            override fun onScanFailed(errorCode: Int) {
                onScanFinished(Result.Error(RepositoryError.UnknownError("Scan failed with error code: $errorCode")))
                Log.e(TAG, "Scan failed with error: $errorCode")
            }
        }

        val filter = ScanFilter.Builder()
            .setDeviceName("MyBLEDevice")
            .build()
        val filters = listOf(filter)

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        bluetoothLeScanner.startScan(filters, settings, scanCallback)
    }

    override fun stopScanning() {
        val scanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                bluetoothDevice = result.device
                Log.d(TAG, "Device found: " + result.device.getName() + " - " + result.device.getAddress())
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e(TAG, "Scan failed with error: $errorCode")
            }
        }
        bluetoothLeScanner.stopScan(scanCallback)
    }

    private fun reset() {
        bluetoothDevice = null
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    override fun startDataReadings() {
        requireNotNull(bluetoothDevice) { "Bluetooth device is not connected" }

        val bluetoothGattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BLE", "Connected to GATT server. Discovering services...")
                    gatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BLE", "Disconnected from GATT server.")
                    gatt.close()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val service = gatt.getService(SERVICE_UUID)
                    val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)

                    if (characteristic != null) {
                        val success = gatt.setCharacteristicNotification(characteristic, true)
                        if (success) {
                            val descriptor = characteristic.getDescriptor(CLIENT_CHAR_CONFIG)
                            gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
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

            }
        }

        bluetoothGatt = bluetoothDevice!!.connectGatt(context, false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE)
        bluetoothGatt!!.connect()
    }

    override fun stopDataReadings() {
        requireNotNull(bluetoothGatt) { "Bluetooth GATT is not connected" }
        bluetoothGatt!!.close()
    }

    companion object {
        private const val TAG = "BLE"
        private val SERVICE_UUID = UUID.fromString("00001234-0000-1000-8000-00805f9b34fb")
        private val CHARACTERISTIC_UUID = UUID.fromString("00005678-0000-1000-8000-00805f9b34fb")
        private val CLIENT_CHAR_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}
