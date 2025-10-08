package org.lpss.motosense.util

import android.util.Log

actual fun logNative(level: Level, tag: String?, msg: String) {
    when (level) {
        is Level.INFO -> Log.i(tag, msg)
        is Level.DEBUG -> Log.d(tag, msg)
        is Level.WARN -> Log.w(tag, msg)
        is Level.ERROR -> Log.e(tag, msg)
    }
}
