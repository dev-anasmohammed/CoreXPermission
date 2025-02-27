package com.dev.anasmohammed.corex.permission.core.callback

import com.dev.anasmohammed.corex.permission.core.scopes.ForwardScope
import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.models.Permission

/**
 * Callback for [PermissionBuilder.onForwardToSettings] method.
 */
interface ForwardToSettingsCallback {
    /**
     * Called when you should tell user to allow these permissions in settings.
     * @param scope Scope to show rationale dialog.
     * @param deniedList Permissions that should allow in settings.
     */
    fun onForwardToSettings(scope: ForwardScope , deniedList: List<Permission>)
}