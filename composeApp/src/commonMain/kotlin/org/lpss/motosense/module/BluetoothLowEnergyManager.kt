package org.lpss.motosense.module

import org.lpss.motosense.model.DeviceData
import org.lpss.motosense.util.Result

interface BluetoothLowEnergyManager {
    fun startScanning(onScanFinished: (Result<String>) -> Unit)

    fun stopScanning()

    fun reset()

    fun startDataReadings(
        deviceName: String,
        onDeviceDataReceived: (DeviceData) -> Unit,
    )

    fun stopDataReadings()
}
