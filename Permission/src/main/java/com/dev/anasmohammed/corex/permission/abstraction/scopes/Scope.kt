package com.dev.anasmohammed.corex.permission.abstraction.scopes

import com.dev.anasmohammed.corex.permission.abstraction.dialog.BaseDialogFragment
import com.dev.anasmohammed.corex.permission.core.models.DialogAttrs
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog
/**
 * Interface representing the scope for showing permission request dialogs.
 * Implementations of this interface define how to display dialogs related to permission requests,
 * either through default dialogs or custom ones.
 *
 * Key responsibilities:
 * - Showing default dialogs with a simple UI, without needing a custom implementation.
 * - Allowing the display of custom dialogs by passing a custom [BaseDialogFragment].*
 */
interface Scope {
    /**
     * Show a default dialog no need for custom UI.
     * @param permissions Permissions that to request.
     * @param attrs the attributes of default dialog [CoreXDefaultDialog].
     */
    fun showDefaultDialog(
        permissions: List<Permission>,
        attrs: DialogAttrs
    )

    /**
     * Show a custom dialog.
     * @param dialogFragment custom dialog fragment that inherit from [BaseDialogFragment].
     */
    fun showDialogFragment(dialogFragment: BaseDialogFragment)
}