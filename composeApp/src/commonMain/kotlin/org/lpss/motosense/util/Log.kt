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
)

object Log {
    fun i(tag: String?, msg: String) {
        logNative(Level.INFO, tag, msg)
    }

    fun d(tag: String?, msg: String) {
        logNative(Level.DEBUG, tag, msg)
    }

    fun w(tag: String?, msg: String) {
        logNative(Level.WARN, tag, msg)
    }

    fun e(tag: String?, msg: String) {
        logNative(Level.ERROR, tag, msg)
    }

    fun log(level: Level, tag: String?, msg: String) {
        when (level) {
            is Level.INFO -> i(tag, msg)
            is Level.DEBUG -> d(tag, msg)
            is Level.WARN -> w(tag, msg)
            is Level.ERROR -> e(tag, msg)
        }
    }
}
