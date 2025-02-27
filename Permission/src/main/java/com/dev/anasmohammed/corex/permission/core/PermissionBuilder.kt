package com.dev.anasmohammed.corex.permission.core

import android.app.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.core.callback.ExplainPermissionCallback
import com.dev.anasmohammed.corex.permission.core.callback.ForwardToSettingsCallback
import com.dev.anasmohammed.corex.permission.core.callback.PermissionRequestCallback
import com.dev.anasmohammed.corex.permission.core.chain.Chain
import com.dev.anasmohammed.corex.permission.core.handlers.NormalPermissionsHandler
import com.dev.anasmohammed.corex.permission.core.models.DialogAttrs
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.scopes.ExplainScope
import com.dev.anasmohammed.corex.permission.core.scopes.ForwardScope
import com.dev.anasmohammed.corex.permission.core.utils.getCurrentSdkVersion
import com.dev.anasmohammed.corex.permission.core.utils.getTargetSdkVersion
import com.dev.anasmohammed.corex.permission.core.utils.lockScreenOrientation
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.core.utils.restoreOrientation
import com.dev.anasmohammed.corex.permission.presentation.fragment.PermissionFragment

/**
 * This class react as bridge between [PermissionMediator] and [PermissionFragment].
 * As it take permission from [PermissionMediator] after sort it between normal , special or remove it
 * to request or end request that called in [PermissionFragment]
 * Also Its holds the context , permissions , callbacks and preferences of the library
 */
class PermissionBuilder(
    fragmentActivity: FragmentActivity?,
    fragment: Fragment?,
    internal var normalPermissions: MutableSet<Permission>,
    internal var specialPermissions: MutableSet<Permission>,
    private val permissionMediator: PermissionMediator
) {

    /**
     * The activity that library will show [PermissionFragment] and dialogs on it
     */
    internal lateinit var activity: FragmentActivity

    /**
     * Get the FragmentManager if it's in Activity, or the ChildFragmentManager if it's in Fragment.
     * @return The FragmentManager to operate Fragment.
     */
    private val fragmentManager: FragmentManager
        get() {
            return activity.supportFragmentManager
        }

    /**
     * Get the invisible fragment in activity for request permissions.
     * If there is no invisible fragment, add one into activity.
     */
    internal val permissionFragment: PermissionFragment
        get() {
            val existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
            return if (existedFragment != null) {
                existedFragment as PermissionFragment
            } else {
                val permissionFragment = PermissionFragment()
                fragmentManager.beginTransaction().add(permissionFragment, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
                permissionFragment
            }
        }

    /**
     * Instance of the current dialog that shows to user.
     * We need to dismiss this dialog when [PermissionFragment] destroyed.
     */
    @JvmField
    var currentDialog: Dialog? = null

    /**
     * Indicates should CoreXPermission explain permission before request.
     */
    @JvmField
    var isShowDialogBeforeRequest = false

    /**
     * Indicates is dialog is cancelable or not.
     */
    var isDialogCancelable = true

    /**
     * Indicates the action when dialog's negative button clicked after the dialog is dismissed.
     */
    var onNegativeActionClicked: (() -> Unit)? = null

    /**
     * Holds permissions that have already granted in the requested permissions.
     */
    @JvmField
    var grantedPermissions: MutableSet<Permission> = LinkedHashSet()

    /**
     * Holds all denied permissions [deniedPermissions , permanentDeniedPermissions].
     */
    @JvmField
    var allDeniedPermissions: MutableSet<Permission> = LinkedHashSet()

    /**
     * Holds permissions that have been denied in the requested permissions.
     */
    @JvmField
    var deniedPermissions: MutableSet<Permission> = LinkedHashSet()

    /**
     * Holds permissions that have been permanently denied in the requested permissions.
     * (Deny and never ask again)
     */
    @JvmField
    var permanentDeniedPermissions: MutableSet<Permission> = LinkedHashSet()

    /**
     * When we request multiple permissions. Some are denied, some are permanently denied.
     * Denied permissions will be callback first.
     * And the permanently denied permissions will store in this tempPermanentDeniedPermissions.
     * They will be callback once no more denied permissions exist.
     */
    @JvmField
    var tempPermanentDeniedPermissions: MutableSet<Permission> = LinkedHashSet()

    /**
     * Holds permissions which should forward to Settings to allow them.
     * Not all permanently denied permissions should forward to Settings.
     * Only the ones developer think they are necessary should.
     */
    @JvmField
    var forwardPermissions: MutableSet<Permission> = LinkedHashSet()

    /**
     * The callback for [request] method. Can not be null.
     */
    @JvmField
    var requestCallback: PermissionRequestCallback? = null

    /**
     * The callback for [onExplainPermissionRequest] method.
     */
    @JvmField
    var explainPermissionCallback: ExplainPermissionCallback = getDefaultExplainCallback()

    /**
     * The callback for [onForwardToSettings] method.
     */
    @JvmField
    var forwardToSettingsCallback: ForwardToSettingsCallback = getForwardToSettingsCallback()

    /**
     * Get the targetSdkVersion of current app.
     *
     * @return The targetSdkVersion of current app.
     */
    val targetSdkVersion: Int
        get() = getTargetSdkVersion(activity, null)

    /**
     * Get the currentSdkVersion of current app.
     *
     * @return The currentSdkVersion of current app.
     */
    val currentSdkVersion: Int
        get() = getCurrentSdkVersion()

    init {
        if (fragmentActivity != null) {
            activity = fragmentActivity
        }
        // activity and fragment must not be null at same time
        if (fragmentActivity == null && fragment != null) {
            activity = fragment.requireActivity()
        }
    }

    /**-------------- Request --------------**/

    /**
     * Request permissions at once, and handle request result in the callback.
     *
     * @param action Callback with 3 params. allGranted, grantedList, deniedList.
     */
    fun request(action: (allGranted: Boolean, grantedList: List<Permission>, deniedList: List<Permission>) -> Unit) {
        logMe("PermissionBuilder request1")

        //callback
        requestCallback = object : PermissionRequestCallback {
            override fun onPermissionResult(
                allGranted: Boolean, grantedList: List<Permission>, deniedList: List<Permission>
            ) {
                action(allGranted, grantedList, deniedList)
            }
        }

        //start chain
        startChainOfHandlers()
    }

    /**
     * Request permissions at once, and handle request result in the callback.
     *
     * @param callBack callback for request [PermissionRequestCallback].
     */
    fun request(callBack: PermissionRequestCallback) {
        logMe("PermissionBuilder request2")
        //callback
        requestCallback = callBack

        //start chain
        startChainOfHandlers()
    }

    internal fun endRequest() {
        logMe("PermissionBuilder endRequest")

        // Remove the PermissionFragment from current Activity after request finished.
        removePermissionFragment()

        // Restore the orientation after request finished since it's locked before.
        restoreOrientation(activity)
    }

    /**-------------- Chain --------------**/

    /**
     * Lock the orientation when requesting permissions, or callback maybe missed due to activity destroyed.
     * Then build the request chain. Request NormalPermissions runs first, then SpecialPermission runs.
     */
    private fun startChainOfHandlers() {
        lockScreenOrientation(activity)

        logMe("PermissionBuilder startChainOfHandlers normalPermissions:")
        normalPermissions.forEach {
            logMe("PermissionBuilder startChainOfHandlers normalPermissions${it.value}\n")
        }
        logMe("PermissionBuilder startChainOfHandlers specialPermissions:")
        specialPermissions.forEach {
            logMe("PermissionBuilder startChainOfHandlers specialPermissions ${it.value}\n")
        }
        logMe("\n\n")

        val requestChain = Chain(this)

        //if normal permissions not empty
        if (normalPermissions.isNotEmpty()) {
            logMe("PermissionBuilder startChainOfHandlers normal isNotEmpty")
            requestChain.addHandlerToChain(NormalPermissionsHandler())
        }

        //if special permissions not empty
        specialPermissions.forEach { permission ->
            logMe("PermissionBuilder startChainOfHandlers specialPermissions loop ${permission.value}")
            requestChain.addHandlerToChain(permission.handler)
        }

        logMe("PermissionBuilder startChainOfHandlers runHandler\n")

        //start chain
        requestChain.runHandler()
    }

    /**-------------- Explain --------------**/

    /**
     * Called when permissions need to explain permission request.
     * Typically every time user denies your request would call this method.
     *
     * @param action callback with permissions denied by user.
     * @return PermissionBuilder itself.
     */
    fun onExplainPermissionRequest(action: (scope: ExplainScope, deniedList: List<Permission>) -> Unit): PermissionBuilder {
        logMe("PermissionBuilder onExplainPermissionRequest1")
        explainPermissionCallback = object : ExplainPermissionCallback {
            override fun onExplainPermission(
                scope: ExplainScope,
                deniedList: List<Permission>,
            ) {
                action(scope, deniedList)
            }
        }
        return this
    }

    /**
     * Called when permissions need to explain permission request.
     * Typically every time user denies your request would call this method.
     *
     * @param callback Callback with permissions denied by user.
     * @return PermissionBuilder itself.
     */
    fun onExplainPermissionRequest(callback: ExplainPermissionCallback): PermissionBuilder {
        logMe("PermissionBuilder onExplainPermissionRequest2")
        explainPermissionCallback = callback
        return this
    }

    /**
     * Create default ForwardToSettingsCallback to be used without need to chain the [onExplainPermissionRequest]
     * to the request if you don't need to customize it
     *
     * @return [ExplainPermissionCallback] to be a default callback
     */
    private fun getDefaultExplainCallback(): ExplainPermissionCallback {
        return object : ExplainPermissionCallback {
            override fun onExplainPermission(
                scope: ExplainScope,
                deniedList: List<Permission>,
            ) {
                scope.showDefaultDialog(
                    permissions = deniedList,
                    attrs = DialogAttrs(
                        message = permissionMediator.permissionCategory?.rationaleMessage
                            ?: R.string.cxp_default_message,
                        positiveText = permissionMediator.permissionCategory?.rationalePositiveText
                            ?: R.string.cxp_default_positive_button,
                        negativeText = permissionMediator.permissionCategory?.rationaleNegativeText
                            ?: R.string.cxp_default_negative_button,
                    ),
                )
            }
        }

    }

    /**
     * If you need to show request permission rationale, chain this method in your request syntax.
     * [onExplainPermissionRequest] will be called before permission request.
     *
     * @return PermissionBuilder itself.
     */
    fun showExplainDialogBeforeRequest(show: Boolean): PermissionBuilder {
        isShowDialogBeforeRequest = show
        return this
    }

    /**-------------- ForwardToSettings --------------**/

    /**
     * Called when permissions need to forward to Settings for allowing.
     * Typically user denies your request and checked never ask again would call this method.
     * Remember [onExplainPermissionRequest] is always prior to this method.
     * If [onExplainPermissionRequest] is called, this method will not be called in the same request time.
     *
     * @param action Callback with permissions denied and checked never ask again by user.
     * @return PermissionBuilder itself.
     */
    fun onForwardToSettings(action: (scope: ForwardScope, deniedList: List<Permission>) -> Unit): PermissionBuilder {
        logMe("PermissionBuilder onForwardToSettings1")
        forwardToSettingsCallback = object : ForwardToSettingsCallback {
            override fun onForwardToSettings(
                scope: ForwardScope, deniedList: List<Permission>
            ) {
                logMe("PermissionBuilder onForwardToSettings1 $deniedList")
                action(scope, deniedList)
            }
        }
        return this
    }

    /**
     * Called when permissions need to forward to Settings for allowing.
     * Typically user denies your request and checked never ask again would call this method.
     * Remember [onExplainPermissionRequest] is always prior to this method.
     * If [onExplainPermissionRequest] is called, this method will not be called in the same request time.
     *
     * @param callback Callback with permissions denied and checked never ask again by user.
     * @return PermissionBuilder itself.
     */
    fun onForwardToSettings(callback: ForwardToSettingsCallback): PermissionBuilder {
        forwardToSettingsCallback = callback
        logMe("PermissionBuilder onForwardToSettings2")
        return this
    }

    /**
     * Create default ForwardToSettingsCallback to be used without need to chain the [onForwardToSettings]
     * to the request if you don't need to customize it
     *
     * @return [ForwardToSettingsCallback] to be a default callback
     */
    private fun getForwardToSettingsCallback(): ForwardToSettingsCallback {
        return object : ForwardToSettingsCallback {
            override fun onForwardToSettings(
                scope: ForwardScope, deniedList: List<Permission>
            ) {
                scope.showDefaultDialog(
                    permissions = deniedList,
                    attrs = DialogAttrs(
                        message = permissionMediator.permissionCategory?.settingMessage
                            ?: R.string.cxp_default_message,
                        positiveText = permissionMediator.permissionCategory?.settingPositiveText
                            ?: R.string.cxp_default_positive_button,
                        negativeText = permissionMediator.permissionCategory?.settingNegativeText
                            ?: R.string.cxp_default_negative_button,
                    )
                )
            }
        }
    }

    /**-------------- Dialog --------------**/

    /**
     * Set if dialog is cancelable or not. Used if you want to make dialog not cancelable
     * to be forced showed to user.
     * You can use it with [setDialogNegativeAction] to handle what to do if user click negative button
     *
     * @return PermissionBuilder itself.
     */
    fun setDialogCancelable(cancelable: Boolean): PermissionBuilder {
        isDialogCancelable = cancelable
        return this
    }

    /**
     * Set action on negative button of the dialog when user click on it.
     * You can use it with [setDialogCancelable] to make dialog not cancelable and handle what to do
     * when negative action is clicked
     *
     * @return PermissionBuilder itself.
     */
    fun setDialogNegativeAction(action: () -> Unit): PermissionBuilder {
        onNegativeActionClicked = action
        return this
    }

    /**
     * Remove the [PermissionFragment] from current FragmentManager.
     */
    private fun removePermissionFragment() {
        logMe("PermissionBuilder removePermissionFragment")
        val existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
        existedFragment?.let {
            fragmentManager.beginTransaction().remove(existedFragment).commitNowAllowingStateLoss()
        }
    }

    companion object {
        /**
         * TAG of [PermissionFragment] to find and create.
         */
        const val FRAGMENT_TAG = "CoreXPermissionPermissionFragment"
    }
}