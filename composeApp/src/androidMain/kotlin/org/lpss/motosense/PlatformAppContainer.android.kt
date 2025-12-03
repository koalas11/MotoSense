package org.lpss.motosense

import android.content.Context
import org.lpss.motosense.debug.DummyBluetoothLowEnergyManager
import org.lpss.motosense.module.AndroidBluetoothLowEnergyManager
import org.lpss.motosense.module.BluetoothLowEnergyManager

class AndroidAppContainer(
    private val context: Context,
): AppContainerImpl(AndroidContext(context)), PlatformAppContainer {
    override val bluetoothLowEnergyManager: BluetoothLowEnergyManager by lazy {
        if (MotoSenseBuildConfig.USE_MOCK_DATA) {
            DummyBluetoothLowEnergyManager()
        } else {
            AndroidBluetoothLowEnergyManager(context)
        }
    }
}
