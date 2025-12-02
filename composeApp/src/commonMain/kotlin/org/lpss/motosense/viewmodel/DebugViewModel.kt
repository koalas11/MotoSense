package org.lpss.motosense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.lpss.motosense.module.DebugBLEDevice

class DebugViewModel(
    private val debugBLEDevice: DebugBLEDevice,
) : ViewModel() {

    fun createDebugDevice() {
        debugBLEDevice.createDebugDevice()
    }

    fun destroyDebugDevice() {
        debugBLEDevice.destroyDebugDevice()
    }

    fun startProducingData() {
        debugBLEDevice.startProducingData()
    }

    fun stopProducingData() {
        debugBLEDevice.stopProducingData()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val debugBLEDevice = motoSenseAppContainer().debugBLEDevice
                DebugViewModel(
                    debugBLEDevice = debugBLEDevice,
                )
            }
        }
    }
}
