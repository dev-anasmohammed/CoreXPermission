package com.dev.anasmohammed.corex.permission.presentation.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.abstraction.dialog.BaseDialogFragment
import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.handlers.base.BasePermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.DialogAttrs
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.isAndroidOreo26
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.core.utils.onRequest.doExplainPermission
import com.dev.anasmohammed.corex.permission.core.utils.onRequest.forwardToSettings
import com.dev.anasmohammed.corex.permission.core.utils.onRequest.onRequestBackgroundLocationPermissionResult
import com.dev.anasmohammed.corex.permission.core.utils.onRequest.onRequestIgnoreBatteryOptimization
import com.dev.anasmohammed.corex.permission.core.utils.onRequest.onRequestNormalPermissionsResult
import com.dev.anasmohammed.corex.permission.core.utils.onRequest.onRequestNotificationPermissionResult
import com.dev.anasmohammed.corex.permission.core.utils.requestMultiNormalPermissions
import com.dev.anasmohammed.corex.permission.core.utils.requestPermission
import com.dev.anasmohammed.corex.permission.core.utils.startActivityForResult
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 * An lightweight invisible fragment embedded into activity for handling permission requests.
 * This fragment is responsible for:
 * 1- request permission and startActivityResult
 * 2- handle callback of this request.
 * 3- show explain and forward to settings dialog [doExplainPermission] and [forwardToSettings]
 *
 * The request of permission called from handlers. i.e any handler implement class [BasePermissionHandler]
 */
class PermissionFragment : Fragment() {

    /** Instance of current builder. **/
    private lateinit var permissionBuilder: PermissionBuilder

    /** Instance of current handler. **/
    private lateinit var permissionHandler: PermissionHandler

    /**
     * Request permissions at once by calling [requestMultiNormalPermissions],
     * and handle request result in [onRequestNormalPermissionsResult].
     *
     * @param permissionBuilder The instance of current builder.
     * @param permissionHandler Instance of current handler.
     * @param permissions Permissions that you want to request.
     */
    fun requestNormalPermissions(
        permissionBuilder: PermissionBuilder,
        permissionHandler: PermissionHandler,
        permissions: Set<Permission>
    ) {
        logMe("PermissionFragment requestNormalPermissions")

        this.permissionBuilder = permissionBuilder
        this.permissionHandler = permissionHandler

        val stringPermissions = permissions.map { it.value }

        requestMultiNormalPermissions(
            "requestMultiNormalPermissions", permissions
        ) { results ->
            onRequestNormalPermissionsResult(
                result = results,
                permissionFragment = this@PermissionFragment,
                permissionBuilder = permissionBuilder,
                permissionHandler = permissionHandler,
            )
        }.launch(stringPermissions.toTypedArray())
    }

    /**
     * Request ACCESS_BACKGROUND_LOCATION at once by calling [requestPermission],
     * and handle request result in [onRequestBackgroundLocationPermissionResult].
     *
     * @param permissionBuilder The instance of current builder.
     * @param permissionHandler The instance of current handler.
     */
    fun requestAccessBackgroundLocationPermission(
        permissionBuilder: PermissionBuilder, permissionHandler: PermissionHandler
    ) {
        this.permissionBuilder = permissionBuilder
        this.permissionHandler = permissionHandler

        //Used to get the result for ACCESS_BACKGROUND_LOCATION permission
        requestPermission("BackgroundLocationLauncher") { result ->
            onRequestBackgroundLocationPermissionResult(
                result = result,
                permissionFragment = this@PermissionFragment,
                permissionBuilder = permissionBuilder,
                permissionHandler = permissionHandler,
            )
        }.launch(CoreXPermissions.BackgroundLocation.value)
    }

    /**
     * Request REQUEST_IGNORE_BATTERY_OPTIMIZATIONS at once by calling [startActivityForResult],
     * and handle request result in [onRequestIgnoreBatteryOptimization].
     *
     * @param permissionBuilder The instance of current builder.
     * @param permissionHandler The instance of current handler.
     */
    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimization(
        permissionBuilder: PermissionBuilder, permissionHandler: PermissionHandler
    ) {
        this.permissionBuilder = permissionBuilder
        this.permissionHandler = permissionHandler

        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:${requireActivity().packageName}")

        //Used to get the result for SYSTEM_ALERT_WINDOW permission
        startActivityForResult("IgnoreBatteryOptimizationLauncher") {
            onRequestIgnoreBatteryOptimization(
                permissionFragment = this@PermissionFragment,
                permissionBuilder = permissionBuilder,
                permissionHandler = permissionHandler
            )
        }.launch(intent)
    }


    /**
     * Request ACTION_APP_NOTIFICATION_SETTINGS at once by calling [startActivityForResult],
     * and handle request result in [onRequestNotificationPermissionResult].
     *
     * @param permissionBuilder The instance of current builder.
     * @param permissionHandler The instance of current handler.
     */
    fun requestNotificationPermission(
        permissionBuilder: PermissionBuilder, permissionHandler: PermissionHandler
    ) {
        this.permissionBuilder = permissionBuilder
        this.permissionHandler = permissionHandler

        //TODO check if this condition is necessary
        if (isAndroidOreo26()) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)

            //Used to get the result for notification permission
            startActivityForResult("NotificationLauncher") {
                onRequestNotificationPermissionResult(
                    permissionFragment = this@PermissionFragment,
                    permissionBuilder = permissionBuilder,
                    permissionHandler = permissionHandler
                )
            }.launch(intent)
        } else {
            onRequestNotificationPermissionResult(
                permissionFragment = this,
                permissionBuilder = permissionBuilder,
                permissionHandler = permissionHandler
            )
        }
    }

    /**-------------- Dialog --------------**/
    /**
     * Show a dialog to user and explain why these permissions are necessary.
     *
     * @param builder Instance of current builder.
     * @param handler Instance of current handler.
     * @param permissions  Permissions to request again.
     * @param isSettingsDialog Indicates should show explain permission or forward to Settings.
     * @param attrs all default dialog attributes [DialogAttrs].
     */
    internal fun showPermissionDialog(
        builder: PermissionBuilder,
        handler: PermissionHandler,
        permissions: List<Permission>,
        isSettingsDialog: Boolean,
        attrs: DialogAttrs
    ) {
        logMe("PermissionBuilder showPermissionDialog")

        this.permissionBuilder = builder
        this.permissionHandler = handler

        val defaultDialog = CoreXDefaultDialog(
            permissions = permissions,
            isSettingsDialog = isSettingsDialog,
            dialogAttrs = attrs,
            canCancelDialog = permissionBuilder.isDialogCancelable,
        )
        showPermissionDialog(handler, isSettingsDialog, defaultDialog)
    }

    /**
     * Show a DialogFragment to user and  explain why these permissions are necessary.
     *
     * @param permissionHandler Instance of current handler.
     * @param isSettingsDialog Indicates should show explain permission or forward to Settings.
     * @param dialogFragment DialogFragment to explain or forward to settings.
     */
    fun showPermissionDialog(
        permissionHandler: PermissionHandler,
        isSettingsDialog: Boolean,
        dialogFragment: BaseDialogFragment
    ) {
        logMe("PermissionBuilder showPermissionDialog2")
        this.permissionHandler = permissionHandler
        logMe("PermissionBuilder isDialogShowed = ${CoreXPermission.isDialogShowed}")
        logMe("PermissionBuilder canRequestAgain = ${CoreXPermission.canRequestAgain}")

        if (CoreXPermission.isDialogShowed) return

        CoreXPermission.isDialogShowed = true
        CoreXPermission.canRequestAgain = false

        //if no permission return 
        val permissions = dialogFragment.getPermissionsToRequest()
        if (permissions.isEmpty()) {
            CoreXPermission.canRequestAgain = true
            permissionHandler.finish(permissionBuilder)
            return
        }

        //show dialog
        logMe("PermissionBuilder showNow")
        dialogFragment.showNow(childFragmentManager, "CoreXPermissionRationaleDialogFragment")

        if (dialogFragment.isAdded) {
            //Positive Button 
            dialogFragment.getPositiveButton().setOnClickListener {
                dialogFragment.dismiss()
                CoreXPermission.isDialogShowed = false

                if (!isSettingsDialog) {
                    logMe("PermissionBuilder positiveButton requestAgain")
                    permissionHandler.requestAgain(permissionBuilder, permissions)
                } else {
                    logMe("PermissionBuilder positiveButton forwardToSettings")
                    permissionBuilder.forwardPermissions.clear()
                    permissionBuilder.forwardPermissions.addAll(permissions)
                    forwardToSettings(
                        this@PermissionFragment,
                        permissionBuilder,
                        permissionHandler
                    )
                }
            }

            //Negative Button 
            dialogFragment.getNegativeButton().setOnClickListener {
                logMe("PermissionBuilder negativeButton dismiss")
                dialogFragment.dismiss()
                CoreXPermission.isDialogShowed = false
                CoreXPermission.canRequestAgain = true

                permissionHandler.finish(permissionBuilder)
                permissionBuilder.onNegativeActionClicked?.invoke()
            }
        }
    }

    /**
     * On some phones, PermissionBuilder and permissionHandler may become null under unpredictable occasions such as GC.
     * They should not be null at this time, so we can do nothing in this case.
     * @return PermissionBuilder and permissionHandler are still alive or not. If not, we should not do any further logic.
     */
    fun verifyCoreXPermissionIsAlive(): Boolean {
        logMe("PermissionFragment verifyCoreXPermissionIsAlive")
        if (!::permissionBuilder.isInitialized || !::permissionHandler.isInitialized || context == null) {
            try {
                Log.e(getString(R.string.library_name), getString(R.string.handler_builder_null))
            } catch (e: Exception) {
                Log.e("CoreXPermission", "PermissionFragment ${e.message}")
            }
            return false
        }
        return true
    }

    /**
     * Dismiss the showing dialog when PermissionFragment destroyed for avoiding window leak problem
     */
    override fun onDestroy() {
        super.onDestroy()
        if (verifyCoreXPermissionIsAlive()) {
            permissionBuilder.currentDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                    CoreXPermission.isDialogShowed = false
                }
            }
        }
    }
}