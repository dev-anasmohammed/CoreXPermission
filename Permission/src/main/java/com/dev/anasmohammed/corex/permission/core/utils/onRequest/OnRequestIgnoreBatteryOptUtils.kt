package com.dev.anasmohammed.corex.permission.core.utils.onRequest

import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.enums.AndroidVersions.Marshmallow6
import com.dev.anasmohammed.corex.permission.core.utils.isAppOnBatteryOptimizationWhitelist
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions
import com.dev.anasmohammed.corex.permission.presentation.fragment.PermissionFragment

fun onRequestIgnoreBatteryOptimization(
    permissionFragment: PermissionFragment,
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler
) {
    if (permissionFragment.verifyCoreXPermissionIsAlive()) {
        if (permissionBuilder.currentSdkVersion >= Marshmallow6.sdk) {
            if (isAppOnBatteryOptimizationWhitelist(permissionBuilder.activity)) {
                permissionHandler.finish(permissionBuilder)
            } else {
                doExplainPermission(
                    permissionBuilder,
                    permissionHandler,
                    listOf(CoreXPermissions.RequestIgnoreBatteryOptimization)
                )
            }
        } else {
            permissionHandler.finish(permissionBuilder)
        }
    }
}