package com.dev.anasmohammed.corex.permission.data

import com.dev.anasmohammed.corex.permission.core.enums.PermissionType
import com.dev.anasmohammed.corex.permission.core.handlers.BackgroundLocationHandler
import com.dev.anasmohammed.corex.permission.core.handlers.ForegroundServiceHandler
import com.dev.anasmohammed.corex.permission.core.handlers.ForegroundServiceLocationHandler
import com.dev.anasmohammed.corex.permission.core.handlers.IgnoreBatteryOptimizationHandler
import com.dev.anasmohammed.corex.permission.core.handlers.NormalPermissionsHandler
import com.dev.anasmohammed.corex.permission.core.handlers.NotificationHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission

/**
 * This class ued to hold the permissions that library handle to not use permission not handled
 * to avoid unexpected crashes.
 */
object CoreXPermissions {
    /** Notification **/
    val Notification = Permission(
        label = "Notifications",
        value = "android.permission.POST_NOTIFICATIONS",
        type = PermissionType.Special,
        handler = NotificationHandler(),
        group = CoreXPermissionGroups.Notification
    )

    /** Location **/
    val FineLocation = Permission(
        label = "Location",
        value = "android.permission.ACCESS_FINE_LOCATION",
        type = PermissionType.Normal,
        handler = NormalPermissionsHandler(),
        group = CoreXPermissionGroups.Location
    )

    val CoarseLocation = Permission(
        label = "Location",
        value = "android.permission.ACCESS_COARSE_LOCATION",
        type = PermissionType.Normal,
        handler = NormalPermissionsHandler(),
        group = CoreXPermissionGroups.Location
    )

    val BackgroundLocation = Permission(
        label = "Background Location",
        value = "android.permission.ACCESS_BACKGROUND_LOCATION",
        type = PermissionType.Special,
        handler = BackgroundLocationHandler(),
        group = CoreXPermissionGroups.BackgroundLocation
    )

    /** Battery **/
    val RequestIgnoreBatteryOptimization = Permission(
        label = "Ignore Battery Optimization",
        value = "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS",
        type = PermissionType.Special,
        handler = IgnoreBatteryOptimizationHandler(),
        group = CoreXPermissionGroups.Battery
    )

    /** Foreground Service **/
    val ForegroundService = Permission(
        label = "Foreground Service",
        value = "android.permission.FOREGROUND_SERVICE",
        type = PermissionType.Special,
        handler = ForegroundServiceHandler()
    )

    val ForegroundServiceLocation = Permission(
        label = "Foreground Service Location",
        value = "android.permission.FOREGROUND_SERVICE_LOCATION",
        type = PermissionType.Special,
        handler = ForegroundServiceLocationHandler()
    )

    val Camera = Permission(
        label = "Camera",
        value = "android.permission.CAMERA",
        type = PermissionType.Normal,
        handler = NormalPermissionsHandler(),
        group = CoreXPermissionGroups.Camera
    )

    val RecordingAudio = Permission(
        label = "Camera",
        value = "android.permission.RECORD_AUDIO",
        type = PermissionType.Normal,
        handler = NormalPermissionsHandler(),
        group = CoreXPermissionGroups.Audio
    )
}