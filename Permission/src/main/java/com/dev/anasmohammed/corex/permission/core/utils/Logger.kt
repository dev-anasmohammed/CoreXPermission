package com.dev.anasmohammed.corex.permission.core.utils

import android.util.Log

/**
 * Logs a message to the logcat with an error priority if logging is enabled.
 *
 * @param msg The message to be logged.
 * @param canLog A flag indicating whether the message should be logged. Defaults to `true`.
 */
fun logMe(msg: String, canLog: Boolean = false) {
    if (canLog) Log.e("NewStructure", msg)
}