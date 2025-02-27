package com.dev.anasmohammed.corex.permission.abstraction.handler

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.scopes.ExplainScope
import com.dev.anasmohammed.corex.permission.core.scopes.ForwardScope
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 * Interface that defines the necessary methods for handling permission requests and managing
 * permission dialogs. Implementations of this interface will define the logic for showing
 * rationale dialogs, requesting permissions, and handling retries when permissions are denied.
 * It provides methods for managing both the explanation and forwarding of permissions requests.
 *
 * Key responsibilities:
 * - Handling permission requests through [request] and [requestAgain].
 * - Providing specific scopes for explaining the permission requests, such as [ExplainScope]
 *   and [ForwardScope].
 * - Finishing the permission handling process and notifying the result via [finish].
 */
interface PermissionHandler {
    /**
     * Get the [ExplainScope] for showing [CoreXDefaultDialog] or custom DialogFragment.
     * @return Instance of ExplainScope.
     */
    fun getExplainScope(permissionBuilder: PermissionBuilder): ExplainScope

    /**
     * Get the [ForwardScope] for showing [CoreXDefaultDialog] or custom DialogFragment.
     * @return Instance of [ForwardScope].
     */
    fun getForwardScope(permissionBuilder: PermissionBuilder): ForwardScope

    /**
     * Do the request logic.
     */
    fun request(permissionBuilder: PermissionBuilder)

    /**
     * Request permissions again when user denied.
     * If permission is denied by user and [ExplainScope.showDefaultDialog] or [ForwardScope.showDefaultDialog] is called,
     * when user clicked positive button, will call this method.
     *
     * @param permissionBuilder instance of permission builder.
     * @param permissions permissions to request again.
     */
    fun requestAgain(permissionBuilder: PermissionBuilder, permissions: List<Permission>)

    /**
     * Finish this handler and notify the request result.
     */
    fun finish(permissionBuilder: PermissionBuilder)
}