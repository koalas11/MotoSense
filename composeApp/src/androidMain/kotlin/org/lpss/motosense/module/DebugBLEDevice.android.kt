package org.lpss.motosense.module

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class AndroidDebugBLEDevice(
    private val context: Context,
) : DebugBLEDevice {
    private var bluetoothManager: BluetoothManager? = null
    private var gattServer: BluetoothGattServer? = null
    private var advertiser: BluetoothLeAdvertiser? = null
    private var advertiseCallback: AdvertiseCallback? = null
    private var dataProducesJob: Job? = null
    private val connectedDevices = mutableSetOf<BluetoothDevice>()
    private val subscribedDevices = mutableSetOf<BluetoothDevice>()
    private var characteristic: BluetoothGattCharacteristic? = null

    override fun createDebugDevice() {
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)

        // Ensure adapter has a recognizable name so scanner can find it by name
        try {
            bluetoothManager?.adapter?.name = "MotosenseDebug"
        } catch (e: Exception) {
            Log.w(TAG, "Could not set adapter name: ${e.message}")
        }

        // Open GATT server and handle connections / descriptor writes
        gattServer = bluetoothManager!!.openGattServer(context, object : BluetoothGattServerCallback() {
            override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    connectedDevices.add(device)
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    connectedDevices.remove(device)
                    subscribedDevices.remove(device)
                }
            }

            override fun onCharacteristicReadRequest(
                device: BluetoothDevice, requestId: Int, offset: Int, characteristic: BluetoothGattCharacteristic
            ) {
                gattServer!!.sendResponse(device, requestId, GATT_SUCCESS, offset, byteArrayOf(0x01, 0x02))
            }

            override fun onDescriptorWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                descriptor: BluetoothGattDescriptor,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                if (descriptor.uuid == CLIENT_CHAR_CONFIG) {
                    if (value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                        subscribedDevices.add(device)
                    } else if (value.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)) {
                        subscribedDevices.remove(device)
                    }
                    if (responseNeeded) {
                        gattServer!!.sendResponse(device, requestId, GATT_SUCCESS, offset, null)
                    }
                } else {
                    if (responseNeeded) {
                        gattServer!!.sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
                    }
                }
            }
        })

        // Build service / characteristic and add to server
        val service = BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)
        val char = BluetoothGattCharacteristic(
            CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_NOTIFY or BluetoothGattCharacteristic.PROPERTY_READ,
            BluetoothGattCharacteristic.PERMISSION_READ
        )
        val cccDescriptor = BluetoothGattDescriptor(CLIENT_CHAR_CONFIG, BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE)
        char.addDescriptor(cccDescriptor)
        service.addCharacteristic(char)
        characteristic = char

        gattServer?.addService(service)

        startAdvertising()
    }

    override fun destroyDebugDevice() {
        stopProducingData()
        stopAdvertising()

        gattServer?.close()
        gattServer = null
        advertiser = null
        advertiseCallback = null
        bluetoothManager = null
        connectedDevices.clear()
        subscribedDevices.clear()
        characteristic = null
    }

    override fun startProducingData() {
        if (dataProducesJob?.isActive == true) return
        dataProducesJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val bytes = byteArrayOf(0x01, 0x02) // mock payload compatible with DeviceData.fromByteArray
                val char = characteristic ?: continue
                char.value = bytes
                val toNotify = subscribedDevices.toList()
                toNotify.forEach { device ->
                    try {
                        gattServer?.notifyCharacteristicChanged(device, char, false)
                    } catch (e: Exception) {
                        Log.w(TAG, "notify failed for ${device.address}: ${e.message}")
                    }
                }
                delay(1000L)
            }
        }
    }

    override fun stopProducingData() {
        dataProducesJob?.cancel()
        dataProducesJob = null
    }

    private fun startAdvertising() {
        try {
            val adapter = bluetoothManager?.adapter ?: return
            advertiser = adapter.bluetoothLeAdvertiser
            if (advertiser == null) {
                Log.w(TAG, "Device does not support peripheral mode (no advertiser)")
                return
            }

            val settings = AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(true)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build()

            val data = AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(ParcelUuid(SERVICE_UUID))
                .build()

            advertiseCallback = object : AdvertiseCallback() {
                override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                    Log.d(TAG, "Advertising started")
                }

                override fun onStartFailure(errorCode: Int) {
                    Log.e(TAG, "Advertising failed: $errorCode")
                }
            }

            advertiser?.startAdvertising(settings, data, advertiseCallback)
        } catch (e: Exception) {
            Log.e(TAG, "startAdvertising exception: ${e.message}")
        }
    }

    private fun stopAdvertising() {
        try {
            advertiser?.stopAdvertising(advertiseCallback)
            advertiseCallback = null
            advertiser = null
            Log.d(TAG, "Advertising stopped")
        } catch (e: Exception) {
            Log.w(TAG, "stopAdvertising exception: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "DebugBLEDevice"
        private val SERVICE_UUID = UUID.fromString("00001234-0000-1000-8000-00805f9b34fb")
        private val CHARACTERISTIC_UUID = UUID.fromString("00005678-0000-1000-8000-00805f9b34fb")
        private val CLIENT_CHAR_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}
