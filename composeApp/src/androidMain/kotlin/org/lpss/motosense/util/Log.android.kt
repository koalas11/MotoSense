package org.lpss.motosense.util

import android.util.Log

actual fun logNative(level: Level, tag: String?, msg: String, tr: Throwable?) {
    when (level) {
        is Level.INFO -> Log.i(tag, msg, tr)
        is Level.DEBUG -> Log.d(tag, msg, tr)
        is Level.WARN -> Log.w(tag, msg, tr)
        is Level.ERROR -> Log.e(tag, msg, tr)
    }
}
