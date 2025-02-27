package com.dev.anasmohammed.corex.permission.core.handlers

import android.util.Log
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.PermissionMediator
import com.dev.anasmohammed.corex.permission.core.enums.AndroidVersions.QuinceTart10
import com.dev.anasmohammed.corex.permission.core.handlers.base.BaseSpecialPermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.getCurrentSdkVersion
import com.dev.anasmohammed.corex.permission.core.utils.isPermissionGranted
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions

/**
 * Implementation for request ACCESS_BACKGROUND_LOCATION permission.
 */
internal class BackgroundLocationHandler : BaseSpecialPermissionHandler() {

    /**
     * Implementation of request ACCESS_BACKGROUND_LOCATION permission
     */
    override fun request(permissionBuilder: PermissionBuilder) {
        logMe("BackgroundLocationHandler request")

        // If app runs under Android Q [10], there's no ACCESS_BACKGROUND_LOCATION permissions.
        // We remove it from request list, but will append it to the request callback as denied permission.
        if (permissionBuilder.currentSdkVersion < QuinceTart10.sdk) {
            permissionBuilder.grantedPermissions.add(CoreXPermissions.BackgroundLocation)
            permissionBuilder.specialPermissions.remove(CoreXPermissions.BackgroundLocation)

            logMe("BackgroundLocationHandler finish1")
            finish(permissionBuilder)
            return
        }

        // ACCESS_BACKGROUND_LOCATION has already granted,
        // we can finish this handler now.
        if (isBackgroundLocationGranted(permissionBuilder)) {
            logMe("BackgroundLocationHandler isPermissionGranted finish")
            finish(permissionBuilder)
            return
        }

        //check of one of location permission granted before request background location
        if (isOneOfLocationPermissionsGranted(permissionBuilder)) {
            logMe("BackgroundLocationHandler isOneOfLocationPermissionsGranted")

            permissionBuilder.explainPermissionCallback.onExplainPermission(
                getExplainScope(permissionBuilder),
                listOf(CoreXPermissions.BackgroundLocation),
            )
            return
        } else {
            Log.e(
                permissionBuilder.activity.getString(R.string.library_name),
                permissionBuilder.activity.getString(R.string.fine_coarse_not_granted),
            )
        }
    }

    /**
     * Implementation of requestAgain ACCESS_BACKGROUND_LOCATION permission
     */
    override fun requestAgain(permissionBuilder: PermissionBuilder, permissions: List<Permission>) {
        Log.e("NewStructure", "BackgroundLocationHandler requestAgain2")
        permissionBuilder.permissionFragment.requestAccessBackgroundLocationPermission(
            permissionBuilder = permissionBuilder,
            permissionHandler = this@BackgroundLocationHandler
        )
    }

    /**
     * Check if ACCESS_BACKGROUND_LOCATION granted or not
     */
    override fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder) {
        if (isBackgroundLocationGranted(permissionBuilder)) {
            permissionBuilder.grantedPermissions.add(CoreXPermissions.BackgroundLocation)
        } else {
            permissionBuilder.allDeniedPermissions.add(CoreXPermissions.BackgroundLocation)
        }
    }

    /**
     * If we request ACCESS_BACKGROUND_LOCATION on below android Quince [10],
     * We don't need to request specially, just request as normal permission.
     */
    override fun adjustPermissionsForSdk(permissionMediator: PermissionMediator) {
        if (getCurrentSdkVersion() >= QuinceTart10.sdk) {
            permissionMediator.specialPermissions.add(CoreXPermissions.BackgroundLocation)
        }else{
            permissionMediator.normalPermissions.remove(CoreXPermissions.BackgroundLocation)
            permissionMediator.specialPermissions.remove(CoreXPermissions.BackgroundLocation)
        }
    }

    /**
     * To request ACCESS_BACKGROUND_LOCATION permission must one of location permissions be granted
     * ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION
     */
    private fun isOneOfLocationPermissionsGranted(permissionBuilder: PermissionBuilder) =
        isPermissionGranted(
            permissionBuilder.activity, CoreXPermissions.FineLocation.value
        ) || isPermissionGranted(
            permissionBuilder.activity, CoreXPermissions.CoarseLocation.value
        )

    /**
     * simplify the check of ACCESS_BACKGROUND_LOCATION permission
     */
    private fun isBackgroundLocationGranted(permissionBuilder: PermissionBuilder): Boolean {
        return isPermissionGranted(
            permissionBuilder.activity,
            CoreXPermissions.BackgroundLocation.value
        )
    }
}