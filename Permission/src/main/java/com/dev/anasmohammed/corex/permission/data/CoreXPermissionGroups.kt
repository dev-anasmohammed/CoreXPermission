package com.dev.anasmohammed.corex.permission.data

import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.core.models.PermissionGroup
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 * This a permission group that used in the library.
 * It used in [CoreXDefaultDialog] to filter permissions that not display same permission twice
 * in permission layout. e.g [CoreXPermissions.FineLocation] and [CoreXPermissions.CoarseLocation]
 * both of them is for location permission
 */
object CoreXPermissionGroups {
    val UnSpecified = PermissionGroup("UnSpecified", android.R.drawable.stat_notify_error)
    val Notification = PermissionGroup("Notification", R.drawable.ic_item_core_x_notification)
    val Location = PermissionGroup("Location", R.drawable.ic_item_core_x_location)
    val BackgroundLocation = PermissionGroup("BackgroundLocation", R.drawable.ic_item_core_x_location)
    val Battery = PermissionGroup("Battery", android.R.drawable.ic_lock_idle_low_battery)
    val Camera = PermissionGroup("Camera", R.drawable.ic_core_x_camera)
    val Audio = PermissionGroup("Audio", R.drawable.ic_core_x_record_audio)
}