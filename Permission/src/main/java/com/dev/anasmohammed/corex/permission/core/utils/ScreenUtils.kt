package com.dev.anasmohammed.corex.permission.core.utils

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import androidx.fragment.app.FragmentActivity

/**
 * The origin request orientation of the current Activity. We need to restore it when
 * permission request finished.
 */
private var originRequestOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

/**
 * Restore the screen orientation. Activity just behave as before locked.
 * Android O [8] has bug that only full screen activity can request orientation,
 * so we need to exclude Android O [8].
 */
@SuppressLint("ObsoleteSdkInt")
fun restoreOrientation(activity: FragmentActivity) {
    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
        activity.requestedOrientation = originRequestOrientation
    }
}

/**
 * Lock the screen orientation. Activity couldn't rotate with sensor.
 * Android O [8] has bug that only full screen activity can request orientation,
 * so we need to exclude Android O [8].
 */
@SuppressLint("SourceLockedOrientationActivity", "ObsoleteSdkInt")
fun lockScreenOrientation(activity: FragmentActivity) {
    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
        originRequestOrientation = activity.requestedOrientation
        val orientation = activity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}