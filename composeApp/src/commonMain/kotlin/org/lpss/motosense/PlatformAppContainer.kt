package org.lpss.motosense

import org.lpss.motosense.module.BluetoothLowEnergyManager

interface PlatformAppContainer: AppContainer {
    val bluetoothLowEnergyManager: BluetoothLowEnergyManager
}
