package org.lpss.motosense.module

interface DebugBLEDevice {
    fun createDebugDevice()
    fun destroyDebugDevice()
    fun startProducingData()
    fun stopProducingData()
}
