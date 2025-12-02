package org.lpss.motosense

import org.lpss.motosense.module.BluetoothLowEnergyManager
import org.lpss.motosense.module.DebugBLEDevice

interface PlatformAppContainer: AppContainer {
    val bluetoothLowEnergyManager: BluetoothLowEnergyManager
    val debugBLEDevice: DebugBLEDevice
}
