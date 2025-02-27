package com.dev.anasmohammed.corex.permission.core.handlers.base

import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.callback.ExplainPermissionCallback
import com.dev.anasmohammed.corex.permission.core.callback.ForwardToSettingsCallback
import com.dev.anasmohammed.corex.permission.core.scopes.ExplainScope
import com.dev.anasmohammed.corex.permission.core.scopes.ForwardScope
import com.dev.anasmohammed.corex.permission.core.utils.logMe

/**
 * Define a [BasePermissionHandler] to implement the duplicate logic codes. No need to implement them in every handler.
 * and [BaseSpecialPermissionHandler] implement this class for special handlers
 */
abstract class BasePermissionHandler : PermissionHandler {
    /**
     * Point to the next handler. When this handler finish will run next handler. If there's no next handler, the request process end.
     */
    @JvmField
    var next: PermissionHandler? = null

    /**
     * Provide specific scopes for [ExplainPermissionCallback] for specific functions to call.
     */
    override fun getExplainScope(permissionBuilder: PermissionBuilder) =
        ExplainScope(permissionBuilder, this)

    /**
     * Provide specific scopes for [ForwardToSettingsCallback] for specific functions to call.
     */
    override fun getForwardScope(permissionBuilder: PermissionBuilder) =
        ForwardScope(permissionBuilder, this)

    /**
     * Provide the implementation of finishing handler and what to do if no handlers next
     * to be applied to all handlers
     */
    override fun finish(permissionBuilder: PermissionBuilder) {
        logMe("BasePermissionHandler finish")
        logMe("BasePermissionHandler isNextNull ${next == null}")
        logMe("BasePermissionHandler isNextNull ${next.toString()}")

        // If there's next handler, then run it.
        next?.request(permissionBuilder) ?: run {
            // If there's no next handler, finish the request process and notify the result
            logMe("BasePermissionHandler no next")

            //collect denied permissions
            permissionBuilder.allDeniedPermissions.addAll(permissionBuilder.deniedPermissions)
            permissionBuilder.allDeniedPermissions.addAll(permissionBuilder.permanentDeniedPermissions)

            //make sure that permissions not repeated
            permissionBuilder.allDeniedPermissions =
                permissionBuilder.allDeniedPermissions.distinctBy { it.value }.toMutableSet()

            //check special permission is granted or not
            permissionBuilder.specialPermissions.forEach { permission ->
                permission.handler.checkIfGrantedBeforeFinish(permissionBuilder)
            }

            //set result of permission call then end request
            if (permissionBuilder.requestCallback != null) {
                logMe("BasePermissionHandler grantedPermissions ${permissionBuilder.grantedPermissions}")
                logMe("BasePermissionHandler allDeniedPermissions ${permissionBuilder.allDeniedPermissions}")
                logMe("BasePermissionHandler deniedPermissions ${permissionBuilder.deniedPermissions}")
                logMe("BasePermissionHandler forwardPermissions ${permissionBuilder.forwardPermissions}")

                permissionBuilder.requestCallback?.onPermissionResult(
                    permissionBuilder.allDeniedPermissions.isEmpty(),
                    ArrayList(permissionBuilder.grantedPermissions),
                    ArrayList(permissionBuilder.allDeniedPermissions),
                )
            }

            logMe("BasePermissionHandler endRequest")
            permissionBuilder.endRequest()

            CoreXPermission.canRequestAgain = true
        }
    }

    /**
     * Used to handle the implementation of checking
     * if each handler of permission is granted or not before finish the handler
     */
    abstract fun checkIfGrantedBeforeFinish(permissionBuilder: PermissionBuilder)
}