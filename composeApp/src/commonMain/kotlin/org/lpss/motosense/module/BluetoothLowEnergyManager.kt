package org.lpss.motosense.module

import org.lpss.motosense.util.Result

interface BluetoothLowEnergyManager {
    fun startScanning(onScanFinished: (Result<Unit>) -> Unit)

    fun stopScanning()

    fun startDataReadings()

    fun stopDataReadings()
}
