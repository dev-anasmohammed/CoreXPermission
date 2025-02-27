package com.dev.anasmohammed.corex.permission.core.handlers

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.PermissionMediator
import com.dev.anasmohammed.corex.permission.core.enums.AndroidVersions.Pie9
import com.dev.anasmohammed.corex.permission.core.handlers.base.BaseSpecialPermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.getCurrentSdkVersion
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions

/**
 * Implementation for request android.permission.FOREGROUND_SERVICE permission.
 */
internal class ForegroundServiceHandler internal constructor() : BaseSpecialPermissionHandler() {
    /**
     * No need to do anything here because we remove it
     * Or add it to normal permissions depend on android version
     */
    override fun request(permissionBuilder: PermissionBuilder) {}

    /**
     * No need to do anything here because we remove it
     * Or add it to normal permissions depend on android version
     */
    override fun requestAgain(
        permissionBuilder: PermissionBuilder,
        permissions: List<Permission>
    ) {}

    /**
     * No need to do anything here because we remove it
     * Or add it to normal permissions depend on android version
     */
    override fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder) {}

    /**
     * If we request FOREGROUND_SERVICE on Pie9 or above
     * We don't need to request specially, just request as normal permission.
     */
    override fun adjustPermissionsForSdk(permissionMediator: PermissionMediator) {
        permissionMediator.specialPermissions.remove(CoreXPermissions.ForegroundService)
        if (getCurrentSdkVersion() >= Pie9.sdk) {
            permissionMediator.normalPermissions.add(CoreXPermissions.ForegroundService)
        }
    }
}