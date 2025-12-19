package org.lpss.motosense.debug

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.lpss.motosense.model.DeviceData
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Generates realistic dummy device data for testing purposes.
 */
class DeviceDataGenerator {
    private var lastRoll: Int = 0
    private var lastPitch: Int = 0
    private var lastAltitude: Int = 0
    private var lastSpeed: Int = 0
    private var lastLatitude: Float = 45.4642f   // start near Milan
    private var lastLongitude: Float = 9.19f

    @OptIn(ExperimentalTime::class)
    fun next(): DeviceData {
        // roll/pitch evolve gradually
        lastRoll = (lastRoll + Random.nextInt(-2, 3)).coerceIn(-128, 127)
        lastPitch = (lastPitch + Random.nextInt(-2, 3)).coerceIn(-128, 127)

        // altitude changes slowly
        lastAltitude = (lastAltitude + Random.nextInt(-5, 6)).coerceIn(-2000, 2000)

        // speed changes smoothly
        lastSpeed = (lastSpeed + Random.nextInt(-3, 4)).coerceIn(0, 300)

        // latitude/longitude drift slightly
        lastLatitude += Random.nextDouble(-0.0001, 0.0001).toFloat()
        lastLongitude += Random.nextDouble(-0.0001, 0.0001).toFloat()

        // perceived acceleration depends on speed changes
        val perceivedAcceleration = (lastSpeed / 300.0f) * Random.nextDouble(-1.0, 1.0).toFloat()

        // acceleration direction random but bounded
        val accelerationDirection = Random.nextInt(0, 8).toByte()

        // current time
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        return DeviceData(
            rollAngle = lastRoll.toShort(),
            pitchAngle = lastPitch.toShort(),
            slopeAngle = 100 * kotlin.math.tan((lastPitch.toDouble() * kotlin.math.PI) / 180.0),
            perceivedAcceleration = perceivedAcceleration,
            accelerationDirection = accelerationDirection,
            altitude = lastAltitude.toShort(),
            speed = lastSpeed.toUShort(),
            latitude = lastLatitude,
            longitude = lastLongitude,
            hour = now.hour.toByte(),
            minute = now.minute.toByte(),
            second = now.second.toByte(),
            day = now.day.toByte(),
            month = now.month.number.toByte(),
            year = (now.year - 2000).toByte(),
            timestamp = now.toInstant(TimeZone.UTC).toEpochMilliseconds()
        )
    }
}
