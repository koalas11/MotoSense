package org.lpss.motosense

import org.lpss.motosense.model.DeviceData
import kotlin.test.Test

class DeviceDataConversionsTest {

    @Test
    fun testRealConversion() {
        val byteArray = byteArrayOf(
            62, 64, 64, 0, 0, 0, 63, 255.toByte(),
            224.toByte(), 4, 0, 0, 75, 8, 0, 0,
            75, 11, 255.toByte(), 254.toByte(), 15, 254.toByte()
        )

        val data = DeviceData.fromByteArray(byteArray)
        println(data)

        val ints = byteArrayOf(
            36,   // 00100100
            -84,  // 10101100
            85,   // 01010101
            -99,  // 10011101
            37,   // 00100101
            -1,   // 11111111
            3,    // 00000011
            -1,   // 11111111
            -32,  // 11100000
            4,    // 00000100
            0,    // 00000000
            0,    // 00000000
            75,   // 01001011
            8,    // 00001000
            0,    // 00000000
            0,    // 00000000
            75,   // 01001011
            11,   // 00001011
            -1,   // 11111111
            -2,   // 11111110
            15,   // 00001111
            -2    // 11111110
        )

        val data2 = DeviceData.fromByteArray(ints)
        println(data2)
    }
}
