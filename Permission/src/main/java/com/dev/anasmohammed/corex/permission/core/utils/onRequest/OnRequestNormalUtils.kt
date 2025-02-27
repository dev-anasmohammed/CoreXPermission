package com.dev.anasmohammed.corex.permission.core.utils.onRequest

import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.isPermissionGranted
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.presentation.fragment.PermissionFragment

/**
 * Handle result of normal permissions request.
 */
fun onRequestNormalPermissionsResult(
    result: Map<Permission, Boolean>,
    permissionFragment: PermissionFragment,
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler
) {
    logMe("PermissionFragment onRequestNormalPermissionsResult")
    if (permissionFragment.verifyCoreXPermissionIsAlive()) {

        // User may turn some permissions off in settings. So request them again,
        permissionBuilder.grantedPermissions.clear()

        // holds denied permissions in the request permissions.
        val permissionNeedToExplain: MutableSet<Permission> = mutableSetOf()

        // hold permanently denied permissions in the request permissions.
        val permissionsNeedToForward: MutableSet<Permission> = mutableSetOf()

        for ((permission, granted) in result) {
            logMe("PermissionFragment onNormalResult permission ${permission.value}")

            when (granted) {
                true -> {
                    logMe("PermissionFragment onNormalResult granted")
                    handleGrantedPermission(permissionBuilder, permission)
                }

                // Denied permission can turn into permanent denied permissions,
                // but permanent denied permission can not turn into denied permissions.
                false -> {
                    logMe("PermissionFragment onNormalResult not granted")
                    if (shouldShowRequestPermissionRationale(
                            permissionBuilder.activity,
                            permission.value
                        )
                    ) {
                        logMe("PermissionFragment onNormalResult shouldShowRationale true")

                        // No need to remove the current permission from permanentDeniedPermissions
                        // because it won't be there.
                        permissionNeedToExplain.add(permission)
                        permissionBuilder.deniedPermissions.add(permission)
                    } else {
                        logMe("PermissionFragment onNormalResult shouldShowRationale false")

                        // We must remove the current permission from deniedPermissions
                        // because it is permanent denied permission now.
                        permissionsNeedToForward.add(permission)
                        permissionBuilder.permanentDeniedPermissions.add(permission)
                        permissionBuilder.deniedPermissions.remove(permission)
                    }
                }
            }
        }

        // used to validate the deniedPermissions and permanentDeniedPermissions
        // maybe user can turn some permissions on in settings that we didn't request,
        // so check the denied permissions again for safety.
        validateNormalDeniedPermissions(
            permissionBuilder,
            permissionHandler,
            permissionNeedToExplain,
            permissionsNeedToForward
        )
    }
}

private fun validateNormalDeniedPermissions(
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler,
    permissionNeedToExplain: MutableSet<Permission>,
    permissionsNeedToForward: MutableSet<Permission>
) {
    logMe("PermissionFragment validateNormalDeniedPermissions")

    val deniedPermissions: MutableSet<Permission> = mutableSetOf()
    deniedPermissions.addAll(permissionBuilder.deniedPermissions)
    deniedPermissions.addAll(permissionBuilder.permanentDeniedPermissions)
    deniedPermissions.addAll(permissionBuilder.allDeniedPermissions)

//    logMe("PermissionFragment validateNormalDeniedPermissions deniedPermissions111 ${permissionBuilder.deniedPermissions}")
//    logMe("PermissionFragment validateNormalDeniedPermissions grantedPermissions ${permissionBuilder.grantedPermissions}")
//    logMe("PermissionFragment validateNormalDeniedPermissions deniedPermissions ${permissionBuilder.deniedPermissions}")
//    logMe("PermissionFragment validateNormalDeniedPermissions allDeniedPermissions ${permissionBuilder.allDeniedPermissions}")
//    logMe("PermissionFragment validateNormalDeniedPermissions permanentDeniedPermissions ${permissionBuilder.permanentDeniedPermissions}")
//    logMe("PermissionFragment validateNormalDeniedPermissions tempPermanentDeniedPermissions ${permissionBuilder.tempPermanentDeniedPermissions}")
//    logMe("PermissionFragment validateNormalDeniedPermissions forwardPermissions ${permissionBuilder.forwardPermissions}")

    //recheck if they are granted or not
    deniedPermissions.forEach { permission ->
        logMe("PermissionFragment validateNormalDeniedPermissions permission ${permission.value}")

        if (isPermissionGranted(permissionBuilder.activity, permission.value)) {
            logMe("PermissionFragment validateNormalDeniedPermissions permission granted")
            handleGrantedPermission(permissionBuilder, permission)
        }
    }

    //filter granted permissions
    permissionBuilder.grantedPermissions =
        permissionBuilder.grantedPermissions.distinctBy { it.value }.toMutableSet()

    // If all permissions are granted, finish current handler directly.
    logMe("PermissionFragment validateNormalDeniedPermissions grantedPermissions:${permissionBuilder.grantedPermissions.size}, normalPermissions:${permissionBuilder.normalPermissions.size}")
    if (permissionBuilder.grantedPermissions.size == permissionBuilder.normalPermissions.size) {
        logMe("PermissionFragment validateNormalDeniedPermissions permission all granted")
        logMe("PermissionFragment validateNormalDeniedPermissions permission all finish")
        permissionHandler.finish(permissionBuilder)
        return
    }

    // Indicate if we should finish the handler
    var shouldFinishTheHandler = true

    // If there are denied permissions, Show ExplainDialog.
    if (permissionNeedToExplain.isNotEmpty()) {
        logMe("PermissionFragment validateNormalDeniedPermissions isNeedToExplainPermission")

        // shouldn't because ExplainPermissionCallback handles it
        shouldFinishTheHandler = false

        // store these permanently denied permissions or they will be lost when request again.
        permissionBuilder.tempPermanentDeniedPermissions.addAll(permissionsNeedToForward)

        //explain
        doExplainPermission(
            permissionBuilder,
            permissionHandler,
            permissionBuilder.deniedPermissions.toList()
        )
    } else if (permissionsNeedToForward.isNotEmpty() || permissionBuilder.tempPermanentDeniedPermissions.isNotEmpty()) {
        logMe("PermissionFragment validateNormalDeniedPermissions isNeedToForwardPermission $permissionsNeedToForward")

        // shouldn't because ForwardToSettingsCallback handles it
        shouldFinishTheHandler = false

        // no need to store them anymore once onForwardToSettings callback.
        permissionBuilder.tempPermanentDeniedPermissions.clear()

        // settings
        permissionBuilder.forwardToSettingsCallback.onForwardToSettings(
            permissionHandler.getForwardScope(permissionBuilder),
            ArrayList(permissionBuilder.permanentDeniedPermissions)
        )
    }

    // If ExplainDialog or ForwardToSettingsDialog is not called. We should finish the handler.
    if (shouldFinishTheHandler || !CoreXPermission.isDialogShowed) {
        permissionHandler.finish(permissionBuilder)
        logMe("PermissionFragment validateNormalDeniedPermissions shouldFinishTheHandler")
    }

    logMe("PermissionFragment validateNormalDeniedPermissions end")

    // Reset this value after each request.
    CoreXPermission.isDialogShowed = false
}