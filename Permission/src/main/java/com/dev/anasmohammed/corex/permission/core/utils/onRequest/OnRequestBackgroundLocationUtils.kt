package com.dev.anasmohammed.corex.permission.core.utils.onRequest

import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions
import com.dev.anasmohammed.corex.permission.presentation.fragment.PermissionFragment

/**
 * Handle result of ACCESS_BACKGROUND_LOCATION permission request.
 */
fun onRequestBackgroundLocationPermissionResult(
    result: Boolean,
    permissionFragment: PermissionFragment,
    permissionBuilder: PermissionBuilder,
    permissionHandler: PermissionHandler,
) {
    logMe("PermissionFragment onRequestBackgroundLocationPermissionResult")
    if (permissionFragment.verifyCoreXPermissionIsAlive()) {
        if (result) {
            handleGrantedPermission(permissionBuilder, CoreXPermissions.BackgroundLocation)
            permissionHandler.finish(permissionBuilder)
        } else {
            // If ExplainPermissionCallback is not null and we should show rationale. Try the ExplainPermissionCallback.
            if (shouldShowRequestPermissionRationale(
                    permissionBuilder.activity,
                    CoreXPermissions.BackgroundLocation.value
                )
            ) {
                doExplainPermission(
                    permissionBuilder,
                    permissionHandler,
                    listOf(CoreXPermissions.BackgroundLocation)
                )
            } else {
                permissionBuilder.forwardToSettingsCallback.onForwardToSettings(
                    permissionHandler.getForwardScope(permissionBuilder),
                    listOf(CoreXPermissions.BackgroundLocation)
                )
            }
            // If showExplainDialog or showSettingDialog is not called. We should finish the handler.
            // There's case that ExplainPermissionCallback or ForwardToSettingsCallback is called, but developer didn't invoke
            // showExplainDialog or showSettingDialog in the callback.
            // At this case and all other cases, handler should be finished.
            if (!CoreXPermission.isDialogShowed) {
                permissionHandler.finish(permissionBuilder)
            }
        }
    }
}