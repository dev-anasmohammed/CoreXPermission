package com.dev.anasmohammed.corex.permission.abstraction.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 *  Base DialogFragment class to inherits to display a explain dialog and show user why you need the permissions that you asked.
 *  Your DialogFragment must have a positive button to proceed request and an optional negative button to cancel request. Override
 *  [BaseDialogFragment.getPositiveButton] and [BaseDialogFragment.getNegativeButton] to implement that
 */
 abstract class BaseDialogFragment : DialogFragment() {

    /**
     * Handles the fragment's recreation to prevent duplicate dialogs.
     * If the fragment is being restored from a saved state (e.g., after a configuration change),
     * the dialog is dismissed to avoid overlapping or redundant displays.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            dismiss()
            CoreXPermission.isDialogShowed = false
        }
    }

    /**
     * Return the instance of positive button on the DialogFragment. Your DialogFragment must have a positive button to proceed request.
     * @return The instance of positive button on the DialogFragment.
     */
    abstract fun getPositiveButton(): View

    /**
     * Return the instance of negative button on the DialogFragment.
     * @return The instance of positive button on the DialogFragment.
     */
    abstract fun getNegativeButton(): View

    /**
     * Provide permissions to request. These permissions should be the ones that shows on your [CoreXDefaultDialog].
     * @return Permissions list to request.
     */
    abstract fun getPermissionsToRequest(): List<Permission>

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        CoreXPermission.isDialogShowed = false
        CoreXPermission.canRequestAgain = true
    }
}