package com.dev.anasmohammed.corex.permission.core.utils.onRequest

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.core.utils.startActivityForResult
import com.dev.anasmohammed.corex.permission.presentation.fragment.PermissionFragment

fun handleGrantedPermission(permissionBuilder: PermissionBuilder, permission: Permission) {

    permissionBuilder.grantedPermissions.add(permission)

//    permissionBuilder.deniedPermissions =
//        permissionBuilder.deniedPermissions.distinctBy { it.value }.toMutableSet()
//    permissionBuilder.permanentDeniedPermissions =
//        permissionBuilder.permanentDeniedPermissions.distinctBy { it.value }.toMutableSet()
//    permissionBuilder.allDeniedPermissions =
//        permissionBuilder.allDeniedPermissions.distinctBy { it.value }.toMutableSet()
//    permissionBuilder.forwardPermissions =
//        permissionBuilder.forwardPermissions.distinctBy { it.value }.toMutableSet()

    permissionBuilder.deniedPermissions.remove(permission)
    permissionBuilder.permanentDeniedPermissions.remove(permission)
    permissionBuilder.allDeniedPermissions.remove(permission)
    permissionBuilder.forwardPermissions.removeAll(listOf(permission).toSet())
    logMe("handleGrantedPermission")
}

fun doExplainPermission(
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler,
    permissions: List<Permission>
) {
    val distinctPermissions = permissions.distinctBy { it.value }

    distinctPermissions.forEach {
        logMe("PermissionFragment doExplainPermission ${it.value}")
    }

    permissionBuilder.explainPermissionCallback.onExplainPermission(
        permissionHandler.getExplainScope(permissionBuilder),
        distinctPermissions,
    )
}

/**
 * Go to your app's Settings page to let user turn on the necessary permissions.
 */
fun forwardToSettings(
    permissionFragment: PermissionFragment,
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler,
) {
    logMe("PermissionFragment forwardToSettings ")
    permissionBuilder.forwardPermissions.forEach {
        logMe("PermissionFragment forwardToSettings ${it.value}")
    }

    CoreXPermission.canRequestAgain = true
    CoreXPermission.isDialogShowed = false

    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", permissionBuilder.activity.packageName, null)
    intent.data = uri

    //Used to get the result when user switch back from Settings
    permissionFragment.startActivityForResult("ForwardToSettingsLauncher") {
        if (permissionFragment.verifyCoreXPermissionIsAlive()) {
            logMe("PermissionFragment 111111111111111111111111111111111111111111111111")
            logMe("PermissionFragment canRequestAgain ${CoreXPermission.canRequestAgain}")
            logMe("PermissionFragment isDialogShowed ${CoreXPermission.isDialogShowed}")

            permissionHandler.requestAgain(
                permissionBuilder, ArrayList(permissionBuilder.forwardPermissions)
            )
        }
    }.launch(intent)
}