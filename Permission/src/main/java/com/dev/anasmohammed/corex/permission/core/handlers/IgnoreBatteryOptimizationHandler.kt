package com.dev.anasmohammed.corex.permission.core.handlers

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.PermissionMediator
import com.dev.anasmohammed.corex.permission.core.handlers.base.BaseSpecialPermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.isAppOnBatteryOptimizationWhitelist
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions

/**
 * Implementation for request android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS permission.
 */
internal class IgnoreBatteryOptimizationHandler internal constructor() :
    BaseSpecialPermissionHandler() {

    /**
     * Implementation of request REQUEST_IGNORE_BATTERY_OPTIMIZATIONS permission
     */
    override fun request(permissionBuilder: PermissionBuilder) {
        //if app already add on ignore battery optimization not need to show dialog
        if (isAppOnBatteryOptimizationWhitelist(permissionBuilder.activity)) {
            finish(permissionBuilder)
            return
        }

        requestAgain(permissionBuilder, emptyList())
    }

    /**
     * Implementation of requestAgain REQUEST_IGNORE_BATTERY_OPTIMIZATIONS permission
     */
    override fun requestAgain(permissionBuilder: PermissionBuilder, permissions: List<Permission>) {
        permissionBuilder.permissionFragment.requestIgnoreBatteryOptimization(
            permissionBuilder = permissionBuilder,
            permissionHandler = this@IgnoreBatteryOptimizationHandler
        )
    }

    /**
     * Check if app added to while list or not of ignore battery optimization
     */
    override fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder) {
        if (isAppOnBatteryOptimizationWhitelist(permissionBuilder.activity)) {
            permissionBuilder.grantedPermissions.add(CoreXPermissions.RequestIgnoreBatteryOptimization)
        } else {
            permissionBuilder.allDeniedPermissions.add(CoreXPermissions.RequestIgnoreBatteryOptimization)
        }
    }

    /**
     * No need to implement it in this handler
     */
    override fun adjustPermissionsForSdk(permissionMediator: PermissionMediator) {}
}