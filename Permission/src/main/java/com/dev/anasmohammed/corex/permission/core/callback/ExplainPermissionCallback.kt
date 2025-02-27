package com.dev.anasmohammed.corex.permission.core.callback

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.scopes.ExplainScope

/**
 * Callback for [PermissionBuilder.onExplainPermissionRequest] method.
 */
interface ExplainPermissionCallback {
    /**
     * Called when you should explain why you need these permissions.
     * @param scope Scope to show rationale dialog.
     * @param deniedList Permissions that you should explain.
     */
    fun onExplainPermission(
        scope: ExplainScope,
        deniedList: List<Permission>,
    )
}