package org.lpss.motosense

import org.lpss.motosense.model.DeviceData
import kotlin.test.Test

class DeviceDataConversionsTest {

    @Test
    fun testRealConversion() {
        val byteArray = byteArrayOf(
            32, 64, 64, 0, 0, 0, 63, 255.toByte(),
            224.toByte(), 4, 0, 0, 75, 8, 0, 0,
            75, 11, 255.toByte(), 254.toByte(), 15, 254.toByte()
        )

        val data = DeviceData.fromByteArray(byteArray)
        println(data)

    }
}
