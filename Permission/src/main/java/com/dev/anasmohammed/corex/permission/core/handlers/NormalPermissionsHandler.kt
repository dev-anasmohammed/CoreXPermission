package com.dev.anasmohammed.corex.permission.core.handlers

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.handlers.base.BasePermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.isPermissionGranted
import com.dev.anasmohammed.corex.permission.core.utils.logMe

/**
 * Implementation for request normal permissions.
 */
internal class NormalPermissionsHandler : BasePermissionHandler() {

    /**
     * Implementation of request REQUEST_IGNORE_BATTERY_OPTIMIZATIONS permission
     */
    override fun request(permissionBuilder: PermissionBuilder) {
        logMe("NormalPermissionsHandler request ")
        logMe("NormalPermissionsHandler normalPermissions ")
        permissionBuilder.normalPermissions.forEach {
            logMe("NormalPermissionsHandler ${it.value}\n")
        }
        val requestList = ArrayList<Permission>()

        permissionBuilder.normalPermissions.forEach { permission ->
            if (isPermissionGranted(permissionBuilder.activity, permission.value)) {
                // already granted
                permissionBuilder.grantedPermissions.add(permission)
            } else {
                // still need to request
                requestList.add(permission)
            }
        }

        // all permissions are granted
        if (requestList.isEmpty()) {
            logMe("NormalPermissionsHandler requestList.isEmpty()")
            finish(permissionBuilder)
            return
        }

        //show explain dialog or request without explain dialog
        if (permissionBuilder.isShowDialogBeforeRequest) {
            logMe("NormalPermissionsHandler isShowDialogBeforeRequest onExplainPermission")

            permissionBuilder.isShowDialogBeforeRequest = false
            permissionBuilder.deniedPermissions.addAll(requestList)

            permissionBuilder.explainPermissionCallback.onExplainPermission(
                getExplainScope(permissionBuilder),
                requestList,
            )
        } else {
            logMe("NormalPermissionsHandler requestNormalPermissions")
            // Do the request at once. Always request all permissions no matter they are already granted or not, in case user turn them off in Settings.
            permissionBuilder.permissionFragment.requestNormalPermissions(
                permissionBuilder = permissionBuilder,
                permissionHandler = this,
                permissions = permissionBuilder.normalPermissions,
            )
        }
    }

    /**
     * Implementation of requestAgain Normal Permissions permission
     */
    override fun requestAgain(permissionBuilder: PermissionBuilder, permissions: List<Permission>) {
        logMe("NormalPermissionsHandler requestAgain")

        //request again granted and all requested normal permissions again
        val permissionsToRequestAgain: MutableSet<Permission> = HashSet(permissionBuilder.grantedPermissions)
        permissionsToRequestAgain.addAll(permissions)

        if (permissionsToRequestAgain.isNotEmpty()) {
            logMe("NormalPermissionsHandler requestAgain permissionsToRequestAgain.isNotEmpty()")
            permissionBuilder.permissionFragment.requestNormalPermissions(
                permissionBuilder = permissionBuilder,
                permissionHandler = this@NormalPermissionsHandler,
                permissions = permissionsToRequestAgain
            )
        } else {
            logMe("NormalPermissionsHandler requestAgain finish")
            finish(permissionBuilder)
        }
    }

    /**
     * No need to do anything here
     */
    override fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder) {}
}