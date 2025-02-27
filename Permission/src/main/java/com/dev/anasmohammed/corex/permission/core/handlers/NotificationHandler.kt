package com.dev.anasmohammed.corex.permission.core.handlers

import android.os.Build
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.PermissionMediator
import com.dev.anasmohammed.corex.permission.core.enums.AndroidVersions.Tiramisu13
import com.dev.anasmohammed.corex.permission.core.handlers.base.BaseSpecialPermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.areNotificationsEnabled
import com.dev.anasmohammed.corex.permission.core.utils.getCurrentSdkVersion
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions

/**
 * Implementation for request android.permission.POST_NOTIFICATIONS permission.
 */
internal class NotificationHandler internal constructor() : BaseSpecialPermissionHandler() {

    /**
     * Implementation of request POST_NOTIFICATIONS permission
     */
    override fun request(permissionBuilder: PermissionBuilder) {
        // If app runs under Android TIRAMISU [13], there's no POST_NOTIFICATIONS permission.
        // We remove it from request list, but will append it to the request callback as denied permission.
        if (permissionBuilder.currentSdkVersion < Build.VERSION_CODES.TIRAMISU) {
            permissionBuilder.grantedPermissions.add(CoreXPermissions.Notification)
            permissionBuilder.specialPermissions.remove(CoreXPermissions.Notification)
        }

        // POST_NOTIFICATIONS permission has already granted, we can finish this handler now.
        if (areNotificationsEnabled(permissionBuilder.activity)) {
            finish(permissionBuilder)
            return
        }

        permissionBuilder.explainPermissionCallback.onExplainPermission(
            getExplainScope(permissionBuilder),
            listOf(CoreXPermissions.Notification),
        )
    }

    /**
     * Implementation of requestAgain POST_NOTIFICATIONS permission
     */
    override fun requestAgain(permissionBuilder: PermissionBuilder, permissions: List<Permission>) {
        permissionBuilder.permissionFragment.requestNotificationPermission(
            permissionBuilder = permissionBuilder,
            permissionHandler = this@NotificationHandler
        )
    }

    /**
     * Check if POST_NOTIFICATIONS granted or not
     */
    override fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder) {
        if (areNotificationsEnabled(permissionBuilder.activity)) {
            permissionBuilder.grantedPermissions.add(CoreXPermissions.Notification)
        } else {
            permissionBuilder.allDeniedPermissions.add(CoreXPermissions.Notification)
        }
    }

    /**
     * If we request POST_NOTIFICATIONS on Tiramisu13 or above
     * We don't need to request specially, just request as normal permission.
     */
    override fun adjustPermissionsForSdk(permissionMediator: PermissionMediator) {
        if (getCurrentSdkVersion() >= Tiramisu13.sdk) {
            permissionMediator.specialPermissions.remove(CoreXPermissions.Notification)
            permissionMediator.normalPermissions.add(CoreXPermissions.Notification)
        }
    }
}