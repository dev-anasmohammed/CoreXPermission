package com.dev.anasmohammed.corex.permission.core.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Retrieves the current SDK version of the Android device.
 *
 * @return The current SDK version as an integer.
 */
fun getCurrentSdkVersion() = Build.VERSION.SDK_INT

/**
 * Retrieves the target SDK version of the application.
 *
 * @param activity The [FragmentActivity] instance, if available.
 * @param fragment The [Fragment] instance, if available.
 * @return The target SDK version of the application as an integer.
 * @throws NullPointerException if both `activity` and `fragment` are null.
 */
fun getTargetSdkVersion(activity: FragmentActivity?, fragment: Fragment?): Int {
    return activity?.applicationInfo?.targetSdkVersion
        ?: fragment!!.requireContext().applicationInfo.targetSdkVersion
}

/**
 * Checks if the current Android version is Oreo (API 26) or higher.
 *
 * @return `true` if the device is running Android Oreo (API 26) or later, `false` otherwise.
 */
@SuppressLint("ObsoleteSdkInt")
fun isAndroidOreo26() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

/**
 * Checks if the current Android version is Red Velvet Cake (API 30) or higher.
 *
 * @return `true` if the device is running Android Red Velvet Cake (API 30) or later, `false` otherwise.
 */
fun isAndroidRedVelvet30() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R