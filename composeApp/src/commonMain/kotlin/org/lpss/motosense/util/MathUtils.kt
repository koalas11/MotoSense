package org.lpss.motosense.util

import kotlin.math.pow
import kotlin.math.round

fun roundToDecimals(value: Double, decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return round(value * factor) / factor
}
