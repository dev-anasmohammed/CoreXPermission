package com.dev.anasmohammed.corex.permission.core.utils

import android.content.Context
import androidx.core.app.NotificationManagerCompat

/**
 * A helper function to check are notifications are enabled for current app.
 * @param context
 * Any context, will not be retained.
 * @return Note that if Android version is lower than N (7.0), the return value will always be true.
 */
fun areNotificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}