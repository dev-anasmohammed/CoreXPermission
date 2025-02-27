package com.dev.anasmohammed.corex.permission.core.utils.onRequest

import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.utils.areNotificationsEnabled
import com.dev.anasmohammed.corex.permission.core.utils.isAndroidOreo26
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions
import com.dev.anasmohammed.corex.permission.presentation.fragment.PermissionFragment

/**
 * Handle result of notification permission request.
 */
fun onRequestNotificationPermissionResult(
    permissionFragment: PermissionFragment,
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler
) {
    if (permissionFragment.verifyCoreXPermissionIsAlive()) {
        if (isAndroidOreo26()) {
            if (areNotificationsEnabled(permissionBuilder.activity)) {
                permissionHandler.finish(permissionBuilder)
            } else {
                doExplainPermission(
                    permissionBuilder,
                    permissionHandler,
                    listOf(CoreXPermissions.Notification)
                )
            }
        } else {
            permissionHandler.finish(permissionBuilder)
        }
    }
}