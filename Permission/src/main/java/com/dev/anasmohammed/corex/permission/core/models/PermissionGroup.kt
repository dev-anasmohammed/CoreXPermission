package com.dev.anasmohammed.corex.permission.core.models

import androidx.annotation.DrawableRes
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 * Data Model for [Permission] react as [android.Manifest.permission_group]
 *
 * @param value e.g [android.Manifest.permission_group.LOCATION]
 * @param icon that displayed in dialog [CoreXDefaultDialog] in permissions layout
 */
data class PermissionGroup(
    val value: String,
    @DrawableRes val icon: Int = R.drawable.ic_launcher_foreground,
)
