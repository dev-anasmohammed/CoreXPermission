package com.dev.anasmohammed.corex.permission.core.scopes

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.abstraction.dialog.BaseDialogFragment
import com.dev.anasmohammed.corex.permission.abstraction.handler.PermissionHandler
import com.dev.anasmohammed.corex.permission.abstraction.scopes.Scope
import com.dev.anasmohammed.corex.permission.core.callback.ForwardToSettingsCallback
import com.dev.anasmohammed.corex.permission.core.models.DialogAttrs
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 * Provide specific scopes for [ForwardToSettingsCallback] to give it specific functions to call.
 */
class ForwardScope internal constructor(
    private val builder: PermissionBuilder,
    private val handler: PermissionHandler
) : Scope {
    /**
     * Show a rationale dialog to tell user to allow these permissions in settings.
     * @param permissions Permissions that to request.
     * @param attrs the attributes of default dialog [CoreXDefaultDialog].
     */
    override fun showDefaultDialog(
        permissions: List<Permission>,
        attrs: DialogAttrs
    ) {
        builder.permissionFragment.showPermissionDialog(
            builder = builder,
            handler = handler,
            permissions = permissions,
            isSettingsDialog = true, // always true because its Forward Dialog
            attrs = attrs
        )
    }

    /**
     * Show a rationale dialog to tell user to allow these permissions in settings.
     * @param dialogFragment DialogFragment to explain to user why these permissions are necessary.
     */
    override fun showDialogFragment(dialogFragment: BaseDialogFragment) {
        builder.permissionFragment.showPermissionDialog(
            handler,
            false,
            dialogFragment
        )
    }
}