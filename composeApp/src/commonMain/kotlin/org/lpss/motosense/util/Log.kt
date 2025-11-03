package org.lpss.motosense.util

interface Level {
    object INFO: Level
    object DEBUG: Level
    object ERROR: Level
    object WARN: Level
}

expect fun logNative(
    level: Level,
    tag: String?,
    msg: String,
    tr: Throwable?,
)

object Log {
    fun i(tag: String?, msg: String, tr: Throwable? = null) {
        logNative(Level.INFO, tag, msg, tr)
    }

    fun d(tag: String?, msg: String, tr: Throwable? = null) {
        logNative(Level.DEBUG, tag, msg, tr)
    }

    fun w(tag: String?, msg: String, tr: Throwable? = null) {
        logNative(Level.WARN, tag, msg, tr)
    }

    fun e(tag: String?, msg: String, tr: Throwable? = null) {
        logNative(Level.ERROR, tag, msg, tr)
    }

    @Suppress("unused")
    fun log(level: Level, tag: String?, msg: String) {
        when (level) {
            is Level.INFO -> i(tag, msg)
            is Level.DEBUG -> d(tag, msg)
            is Level.WARN -> w(tag, msg)
            is Level.ERROR -> e(tag, msg)
        }
    }
}
