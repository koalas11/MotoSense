package org.lpss.motosense.debug

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lpss.motosense.model.DeviceData
import org.lpss.motosense.module.BluetoothLowEnergyManager
import org.lpss.motosense.util.Result
import kotlin.time.Duration.Companion.milliseconds

/**
 * A dummy implementation of [BluetoothLowEnergyManager] that simulates device scanning and data readings.
 *
 * This is useful for testing and development without requiring actual Bluetooth hardware.
 */
class DummyBluetoothLowEnergyManager : BluetoothLowEnergyManager {
    private var isReadingData: Boolean = false
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var readJob: Job? = null

    override fun startScanning(onScanFinished: (Result<String>) -> Unit) {
        return onScanFinished(Result.Success("Dummy Device"))
    }

    override fun stopScanning() {
        // No operation needed for dummy implementation
    }

    override fun reset() {
        // No operation needed for dummy implementation
    }

    override fun startDataReadings(
        deviceName: String,
        onDeviceDataReceived: (DeviceData) -> Unit
    ) {
        val generator = DeviceDataGenerator()
        if (isReadingData) return
        isReadingData = true
        readJob = scope.launch {
            while (isReadingData) {
                val deviceData = generator.next()
                onDeviceDataReceived(deviceData)
                delay(500.milliseconds)
            }
        }
    }

    override fun stopDataReadings() {
        isReadingData = false
    }
}