package org.lpss.motosense

import android.content.Context
import org.lpss.motosense.module.AndroidBluetoothLowEnergyManager
import org.lpss.motosense.module.AndroidDebugBLEDevice
import org.lpss.motosense.module.BluetoothLowEnergyManager
import org.lpss.motosense.module.DebugBLEDevice

class AndroidAppContainer(
    private val context: Context,
): AppContainerImpl(AndroidContext(context)), PlatformAppContainer {
    override val bluetoothLowEnergyManager: BluetoothLowEnergyManager by lazy {
        AndroidBluetoothLowEnergyManager(context)
    }

    override val debugBLEDevice: DebugBLEDevice by lazy {
        AndroidDebugBLEDevice(context)
    }
}
