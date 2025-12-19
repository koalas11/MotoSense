package org.lpss.motosense.module

import org.lpss.motosense.model.DeviceData
import org.lpss.motosense.util.Result

/**
 * Interface defining the contract for managing Bluetooth Low Energy (BLE) operations.
 */
interface BluetoothLowEnergyManager {

    /**
     * Starts scanning for BLE devices.
     *
     * @param onScanFinished Callback invoked with the result of the scan operation.
     */
    fun startScanning(onScanFinished: (Result<String>) -> Unit)

    /**
     * Stops the ongoing BLE device scanning.
     */
    fun stopScanning()

    /**
     * Resets the BLE manager to its initial state.
     */
    fun reset()

    /**
     * Starts reading data from the specified BLE device.
     *
     * @param deviceName The name of the device to connect to.
     * @param onDeviceDataReceived Callback invoked when new device data is received.
     */
    fun startDataReadings(
        deviceName: String,
        onDeviceDataReceived: (DeviceData) -> Unit,
    )

    /**
     * Stops reading data from the BLE device.
     */
    fun stopDataReadings()
}
