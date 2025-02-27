package com.dev.anasmohammed.corex.permission.presentation.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.abstraction.dialog.BaseDialogFragment
import com.dev.anasmohammed.corex.permission.core.enums.PermissionType
import com.dev.anasmohammed.corex.permission.core.models.DialogAttrs
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.models.PermissionGroup
import com.dev.anasmohammed.corex.permission.core.utils.setColorOfBackground
import com.dev.anasmohammed.corex.permission.core.utils.setColorOfImage
import com.dev.anasmohammed.corex.permission.core.utils.setColorOfText
import com.dev.anasmohammed.corex.permission.data.CoreXPermissionGroups.UnSpecified
import com.dev.anasmohammed.corex.permission.databinding.CoreXPermissionDefaultDialogBinding
import com.dev.anasmohammed.corex.permission.databinding.CoreXPermissionItemBinding
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Default dialog shown when no custom explain or forwardToSettings dialog is implemented by the developer.
 */
class CoreXDefaultDialog : BaseDialogFragment {

    private lateinit var binding: CoreXPermissionDefaultDialogBinding
    private var permissions: List<Permission>
    private val isSettingsDialog: Boolean
    private val dialogAttrs: DialogAttrs
    private var canCancelDialog: Boolean? = null

    /**
     * Primary constructor
     *
     * @param permissions List of permissions that are required for the app.
     * @param isSettingsDialog Flag to indicate whether this dialog is for opening settings or not.
     * @param dialogAttrs Configuration attributes for customizing the dialog appearance.
     * @param canCancelDialog Flag to specify if the dialog can be canceled by the user.
     */
    constructor(
        permissions: List<Permission>,
        isSettingsDialog: Boolean,
        dialogAttrs: DialogAttrs,
        canCancelDialog: Boolean? = null
    ) {
        this.permissions = permissions
        this.isSettingsDialog = isSettingsDialog
        this.dialogAttrs = dialogAttrs
        this.canCancelDialog = canCancelDialog
    }

    /**
     * Empty constructor (secondary constructor)
     */
    constructor() : this(emptyList(), false, DialogAttrs())

    /**
     * Initializes the dialog style and settings.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CoreXPermissionDefaultDialog)
    }

    /**
     * Inflates the layout for the dialog and sets the layout parameters.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Configure window properties (size, background, gravity)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(
            ResourcesCompat.getDrawable(resources, android.R.color.transparent, null)
        )
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.setCancelable(canCancelDialog ?: true)

        // Inflate layout
        binding = CoreXPermissionDefaultDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Sets up the views (icon, title, message, buttons) when the view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure the views with data and color values
        setupDialogBackground()
        setupIcon()
        setupTitle()
        setupMessage()
        setupButtons()

        //build permissions items
        buildPermissionsItems()

        //animate dialog
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            binding.cardRoot.visibility = View.VISIBLE
        }
    }

    private fun setupDialogBackground() {
        //radius
        binding.cardRoot.radius = requireActivity().resources.getDimension(dialogAttrs.dialogRadius)

        //background
        binding.cardRoot.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), dialogAttrs.dialogBackgroundColor)

        //stroke
        binding.cardRoot.strokeColor =
            ContextCompat.getColor(requireContext(), dialogAttrs.dialogStrokeColor)
        binding.cardRoot.strokeWidth =
            requireActivity().resources.getDimension(dialogAttrs.dialogStrokeWidth).toInt()
    }

    /**
     * Sets the visibility and color of the dialog icon.
     */
    private fun setupIcon() {
        //visibility
        if (!dialogAttrs.showIcon) {
            binding.ivIcon.visibility = View.GONE
            binding.ivIconBackground.visibility = View.GONE
            return
        }

        //icon
        binding.ivIcon.setImageResource(if (isSettingsDialog) dialogAttrs.settingsIcon else dialogAttrs.icon)
        binding.ivIcon.setColorOfImage(dialogAttrs.iconColor)

        //icon background
        binding.ivIconBackground.setColorOfImage(dialogAttrs.iconBackgroundColor)
    }

    /**
     * Sets the title text and its color.
     */
    private fun setupTitle() {
        binding.tvTitle.text =
            getString(if (isSettingsDialog) dialogAttrs.settingsTitle else dialogAttrs.title)
        binding.tvTitle.setColorOfText(dialogAttrs.titleTextColor)
    }

    /**
     * Sets the message text and its color.
     */
    private fun setupMessage() {
        binding.messageText.text = getString(dialogAttrs.message)
        binding.messageText.setColorOfText(dialogAttrs.messageColor)
    }

    /**
     * Sets the text and colors of the positive and negative buttons.
     */
    private fun setupButtons() {
        val shapeMaterial1 = MaterialShapeDrawable().apply {
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(resources.getDimension(dialogAttrs.dialogButtonRadius))
                .build()
        }

        val shapeMaterial2 = MaterialShapeDrawable().apply {
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(resources.getDimension(dialogAttrs.dialogButtonRadius))
                .build()
        }

        //radius
        binding.positiveBtn.background = shapeMaterial1
        binding.negativeBtn.background = shapeMaterial2

        //positive button
        binding.positiveBtn.text = getString(dialogAttrs.positiveText)
        binding.positiveBtn.setColorOfText(dialogAttrs.positiveTextColor)
        binding.positiveBtn.setColorOfBackground(dialogAttrs.positiveButtonColor)

        //negative button
        binding.negativeBtn.text = getString(dialogAttrs.negativeText)
        binding.negativeBtn.setColorOfText(dialogAttrs.negativeTextColor)
        binding.negativeBtn.setColorOfBackground(dialogAttrs.negativeButtonColor)
    }

    /**
     * Provides the positive button instance to continue requesting permissions.
     * @return Positive button instance.
     */
    override fun getPositiveButton(): View {
        return binding.positiveBtn
    }

    /**
     * Provides the positive button instance to cancel requesting permissions.
     * @return Negative button instance.
     */
    override fun getNegativeButton(): View {
        return binding.negativeBtn
    }

    /**
     * Returns the list of permissions that should be requested.
     * @return List of permissions to request.
     */
    override fun getPermissionsToRequest(): List<Permission> {
        return permissions
    }

    /**
     * Builds and adds permission items (with labels and icons) to the dialog.
     * Permissions that belong to the same group are added only once.
     */
    private fun buildPermissionsItems() {
        val tempSet = HashSet<String>()
        permissions.forEach { permission ->
            if (shouldAddPermission(permission, permission.group.value, tempSet)) {
                val itemBinding = createPermissionItem(permission, permission.group)

                // Add the permission item to the layout
                binding.permissionsLayout.addView(itemBinding.root)
                tempSet.add(permission.group.value)
            }
        }
    }

    /**
     * Determines whether a permission should be added based on whether its group has been processed.
     * @param permission The permission to check.
     * @param permissionGroup The group to which the permission belongs.
     * @param tempSet A set to track already processed permissions/groups.
     * @return True if the permission should be added, false otherwise.
     *
     * The function checks if:
     * - The permission is part of the `allSpecialPermissions` list and hasn't been processed already (i.e., not in `tempSet`).
     * - Or, the permission group is not null and hasn't been processed yet (i.e., not in `tempSet`).
     */
    private fun shouldAddPermission(
        permission: Permission, permissionGroup: String, tempSet: HashSet<String>
    ): Boolean {
        return (permission.type == PermissionType.Special
                && permission.group != UnSpecified
                && !tempSet.contains(permission.value)) || !tempSet.contains(permissionGroup)
    }

    /**
     * Creates a permission item view with the permission's label and icon.
     * @param permission The permission to display.
     * @param permissionGroup The group to which the permission belongs.
     * @return A binding object for the permission item view.
     */
    private fun createPermissionItem(
        permission: Permission, permissionGroup: PermissionGroup
    ): CoreXPermissionItemBinding {
        //item binding
        val itemBinding = CoreXPermissionItemBinding.inflate(
            layoutInflater, binding.permissionsLayout, false
        )

        //text
        itemBinding.permissionText.text = permission.label
        itemBinding.permissionText.setColorOfText(dialogAttrs.itemTextColor)

        //icon
        itemBinding.permissionIcon.setImageResource(permissionGroup.icon)
        itemBinding.permissionIcon.imageTintList =
            (ContextCompat.getColorStateList(requireContext(), dialogAttrs.itemIconColor))

        return itemBinding
    }
}