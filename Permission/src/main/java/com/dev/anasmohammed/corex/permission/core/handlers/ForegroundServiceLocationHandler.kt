package com.dev.anasmohammed.corex.permission.core.handlers

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.PermissionMediator
import com.dev.anasmohammed.corex.permission.core.enums.AndroidVersions.UpsideDownCake14
import com.dev.anasmohammed.corex.permission.core.handlers.base.BaseSpecialPermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.getCurrentSdkVersion
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions

/**
 * Implementation for request android.permission.FOREGROUND_SERVICE_LOCATION permission.
 */
class ForegroundServiceLocationHandler : BaseSpecialPermissionHandler() {
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
    ) {
    }

    /**
     * No need to do anything here because we remove it
     * Or add it to normal permissions depend on android version
     */
    override fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder) {}

    /**
     *  If we request FOREGROUND_SERVICE_LOCATION on UpsideDownCake14 or above
     *  We don't need to request specially, just request as normal permission.
     */
    override fun adjustPermissionsForSdk(permissionMediator: PermissionMediator) {
        if (getCurrentSdkVersion() >= UpsideDownCake14.sdk) {
            permissionMediator.specialPermissions.remove(CoreXPermissions.ForegroundServiceLocation)
            permissionMediator.normalPermissions.add(CoreXPermissions.ForegroundServiceLocation)
        }else{
            permissionMediator.specialPermissions.remove(CoreXPermissions.ForegroundServiceLocation)
        }
    }
}