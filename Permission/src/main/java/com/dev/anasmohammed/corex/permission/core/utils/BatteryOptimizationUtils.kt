package com.dev.anasmohammed.corex.permission.core.utils;

import android.content.Context
import android.os.PowerManager
import androidx.fragment.app.FragmentActivity

/**
 * Checks if the app is on the battery optimization allowlist.
 *
 * Apps on this allowlist are exempted from most battery-saving restrictions,
 * meaning the system will not apply aggressive power-saving features to the app.
 * This is useful for apps that need to run uninterrupted in the background.
 *
 * @param activity The activity context used to access the PowerManager service.
 * @return True if the app is on the battery optimization allowlist, false otherwise.
 */
fun isAppOnBatteryOptimizationWhitelist(activity: FragmentActivity): Boolean {
    val powerManager = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(activity.packageName)
}