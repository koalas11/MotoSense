package org.lpss.motosense.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable
import org.lpss.motosense.util.Log
import kotlin.experimental.inv
import kotlin.math.PI
import kotlin.math.tan
import kotlin.time.ExperimentalTime

@Serializable
data class DeviceData(
    val rollAngle: Short?,
    val pitchAngle: Short?,
    val slopeAngle: Double?,
    val perceivedAcceleration: Float?,
    val accelerationDirection: Byte?,
    val altitude: Short?,
    val speed: UShort?,
    val latitude: Float?,
    val longitude: Float?,
    val hour: Byte?,
    val minute: Byte?,
    val second: Byte?,
    val day: Byte?,
    val month: Byte?,
    val year: Byte?,
    val timestamp: Long?,
) {
    companion object {
        const val OFFSET_ROLL_ANGLE = 0      // Short (1 sign, 8)
        const val OFFSET_PITCH_ANGLE = 9     // Short (1 sign, 8)
        const val OFFSET_PERCEIVED_ACC = 18   // Short (32)
        const val OFFSET_ACC_DIRECTION = 50   // Byte  (1)
        const val OFFSET_ALTITUDE = 54        // Short (2)
        const val OFFSET_SPEED = 68           // UShort(2)
        const val OFFSET_LATITUDE = 78       // Float (4)
        const val OFFSET_LONGITUDE = 110      // Float (4)
        const val OFFSET_HOUR = 142           // Byte  (1)
        const val OFFSET_MINUTE = 147         // Byte  (1)
        const val OFFSET_SECOND = 153         // Byte  (1)
        const val OFFSET_DAY = 159            // Byte  (1)
        const val OFFSET_MONTH = 164          // Byte  (1)
        const val OFFSET_YEAR = 168           // Byte  (1)

        const val ROLL_ANGLE_NAN: Short = 255
        const val PITCH_ANGLE_NAN: Short = 255
        const val PERCEIVED_ACC_NAN: Float = 16f
        const val ACCELERATION_DIRECTION_NAN: Byte = 16
        const val ALTITUDE_NAN: Short = 8191
        const val SPEED_NAN: UShort = 511u
        const val LATITUDE_NAN = 200f
        const val LONGITUDE_NAN = 200f
        const val HOUR_NAN: Byte = 31
        const val MINUTE_NAN: Byte = 63
        const val SECOND_NAN: Byte = 63
        const val DAY_NAN: Byte = 0
        const val MONTH_NAN: Byte = 15
        const val YEAR_NAN: Byte = 127

        const val TOTAL_LENGTH = 174

        private fun has(byteArray: ByteArray, offsetBits: Int, lenBits: Int): Boolean =
            offsetBits >= 0 && lenBits >= 0 && offsetBits + lenBits <= byteArray.size * 8

        private fun readBitsLE(
            byteArray: ByteArray,
            offsetBits: Int,
            lenBits: Int,
            unsigned: Boolean = false
        ): Int {
            var result = 0
            for (i in 0 until lenBits) {
                val bitIndex = offsetBits + i
                val b = byteArray[bitIndex / 8].toInt() and 0xFF
                // flip bit numbering: MSB (bit 7) is position 0
                val bit = (b shr (7 - (bitIndex % 8))) and 1
                result = result or (bit shl i)
            }

            // sign‑extend if needed
            if (!unsigned && lenBits < 32 && (result and (1 shl (lenBits - 1))) != 0) {
                result = result or ((-1) shl lenBits)
            }

            return result
        }

        private fun readAbsSignLE(byteArray: ByteArray, offsetBits: Int, lenBits: Int): Int {
            val magnitudeBits = lenBits - 1
            var magnitude = 0
            for (i in 0 until magnitudeBits) {
                val bitIndex = offsetBits + i
                val b = byteArray[bitIndex / 8].toInt() and 0xFF
                val bit = (b shr (7 - (bitIndex % 8))) and 1   // <-- flipped order
                magnitude = magnitude or (bit shl i)
            }

            // read the sign bit (the last one)
            val signBitIndex = offsetBits + magnitudeBits
            val b = byteArray[signBitIndex / 8].toInt() and 0xFF
            val signBit = (b shr (7 - (signBitIndex % 8))) and 1
            return if (signBit == 1) -magnitude else magnitude
        }

        private fun readByte(byteArray: ByteArray, offsetBits: Int, lenBits: Int = 8, unsigned: Boolean = false): Byte =
            readBitsLE(byteArray, offsetBits, lenBits, unsigned).toByte()

        private fun readShortLE(byteArray: ByteArray, offsetBits: Int, lenBits: Int = 16): Short =
            readAbsSignLE(byteArray, offsetBits, lenBits).toShort()

        private fun readUnsignedShortLE(byteArray: ByteArray, offsetBits: Int, lenBits: Int = 16): UShort =
            readBitsLE(byteArray, offsetBits, lenBits, unsigned = true).toUShort()

        private fun readIntLE(byteArray: ByteArray, offsetBits: Int): Int =
            readAbsSignLE(byteArray, offsetBits, 32)

        private fun readFloatLE(byteArray: ByteArray, offsetBits: Int): Float =
            Float.fromBits(readIntLE(byteArray, offsetBits))

        private fun reverseBitsInPlace(bytes: ByteArray) {
            for (i in bytes.indices) {
                val b = bytes[i].toInt() and 0xFF
                var rev = 0
                for (j in 0 until 8) {
                    rev = (rev shl 1) or ((b shr j) and 1)
                }
                bytes[i] = rev.toByte()
            }
        }

        fun fromByteArray(byteArray: ByteArray): DeviceData {
            if (byteArray.size * 8 < TOTAL_LENGTH) {
                Log.d(TAG, "fromByteArray: byte array too short: size=${byteArray.size} bytes")
                throw IllegalArgumentException("Byte array is too short to parse DeviceData")
            }

            reverseBitsInPlace(byteArray)

            val rollAngle: Short? = if (has(byteArray, OFFSET_ROLL_ANGLE, 9)) {
                val s = readShortLE(byteArray, OFFSET_ROLL_ANGLE, 9)
                if (s != ROLL_ANGLE_NAN) s else null
            } else null

            val pitchAngle: Short? = if (has(byteArray, OFFSET_PITCH_ANGLE, 9)) {
                val s = readShortLE(byteArray, OFFSET_PITCH_ANGLE, 9)
                if (s != PITCH_ANGLE_NAN) s else null
            } else null

            val slopeAngle = if (pitchAngle != null) {
                    100 * tan(
                        (pitchAngle.toDouble() * PI) / 180.0
                    )
            } else {
                null
            }

            val perceivedAcceleration: Float? = if (has(byteArray, OFFSET_PERCEIVED_ACC, 32)) {
                val s = readFloatLE(byteArray, OFFSET_PERCEIVED_ACC)
                if (s != PERCEIVED_ACC_NAN) s else null
            } else null

            val accelerationDirection: Byte? = if (has(byteArray, OFFSET_ACC_DIRECTION, 4)) {
                val b = readByte(byteArray, OFFSET_ACC_DIRECTION, 4, unsigned = true)
                if (b != ACCELERATION_DIRECTION_NAN) b else null
            } else null

            val altitude: Short? = if (has(byteArray, OFFSET_ALTITUDE, 14)) {
                val s = readShortLE(byteArray, OFFSET_ALTITUDE, 14)
                if (s != ALTITUDE_NAN) s else null
            } else null

            val speed: UShort? = if (has(byteArray, OFFSET_SPEED, 10)) {
                val s = readUnsignedShortLE(byteArray, OFFSET_SPEED, 10)
                if (s != SPEED_NAN) s else null
            } else null

            val latitude: Float? = if (has(byteArray, OFFSET_LATITUDE, 32)) {
                val f = readFloatLE(byteArray, OFFSET_LATITUDE)
                if (f.isFinite() && f != LATITUDE_NAN) f else null
            } else null

            val longitude: Float? = if (has(byteArray, OFFSET_LONGITUDE, 32)) {
                val f = readFloatLE(byteArray, OFFSET_LONGITUDE)
                if (f.isFinite() && f != LONGITUDE_NAN) f else null
            } else null

            val hour: Byte? = if (has(byteArray, OFFSET_HOUR, 5)) {
                val b = readByte(byteArray, OFFSET_HOUR, 5, unsigned = true)
                if (b != HOUR_NAN) b else null
            } else null

            val minute = if (has(byteArray, OFFSET_MINUTE, 6)) {
                val b = readByte(byteArray, OFFSET_MINUTE, 6, unsigned = true)
                if (b != MINUTE_NAN) b else null
            } else null

            val second = if (has(byteArray, OFFSET_SECOND, 6)) {
                val b = readByte(byteArray, OFFSET_SECOND, 6, unsigned = true)
                if (b != SECOND_NAN) b else null
            } else null

            val day = if (has(byteArray, OFFSET_DAY, 5)) {
                val b = readByte(byteArray, OFFSET_DAY, 5, unsigned = true)
                if (b != DAY_NAN) b else null
            } else null

            val month = if (has(byteArray, OFFSET_MONTH, 4)) {
                val b = readByte(byteArray, OFFSET_MONTH, 4, unsigned = true)
                if (b != MONTH_NAN) b else null
            } else null

            val year = if (has(byteArray, OFFSET_YEAR, 7)) {
                val b = readByte(byteArray, OFFSET_YEAR, 7, unsigned = true)
                if (b != YEAR_NAN) b else null
            } else null

            return DeviceData(
                rollAngle = rollAngle,
                pitchAngle = pitchAngle,
                slopeAngle = slopeAngle,
                perceivedAcceleration = perceivedAcceleration,
                accelerationDirection = accelerationDirection,
                altitude = altitude,
                speed = speed,
                latitude = latitude,
                longitude = longitude,
                hour = hour,
                minute = minute,
                second = second,
                day = day,
                month = month,
                year = year,
                timestamp = computeTimestamp(year, month, day, hour, minute, second)
            )
        }

        @OptIn(ExperimentalTime::class)
        private fun computeTimestamp(
            year: Byte?, month: Byte?, day: Byte?,
            hour: Byte?, minute: Byte?, second: Byte?
        ): Long? {
            if (year == null || month == null || day == null ||
                hour == null || minute == null || second == null) return null

            val y = 2000 + year.toInt()
            return try {
                val ldt = LocalDateTime(
                    y,
                    month.toInt(),
                    day.toInt(),
                    hour.toInt(),
                    minute.toInt(),
                    second.toInt()
                )
                ldt.toInstant(TimeZone.UTC).toEpochMilliseconds()
            } catch (_: Exception) {
                Log.d(TAG, "computeTimestamp: invalid date components: " +
                        "year=$y, month=$month, day=$day, " +
                        "hour=$hour, minute=$minute, second=$second")
                null
            }
        }

        const val TAG = "DeviceData"
    }
}
