package com.dev.anasmohammed.corex.permission.core.models

import com.dev.anasmohammed.corex.permission.core.enums.PermissionType
import com.dev.anasmohammed.corex.permission.core.handlers.base.BasePermissionHandler
import com.dev.anasmohammed.corex.permission.data.CoreXPermissionGroups.UnSpecified

/**
 * This a data class of permission that handled by the library
 *
 * @param label this the display name of permission that showed to the user
 * @param value the value of permission from the android system e.g [android.Manifest.permission.ACCESS_FINE_LOCATION]
 * @param type type of permission normal or special
 * @param handler type of handle that will request , request again or finish the request of the permission
 * @param group the group that this permission belongs to. It react like [android.Manifest.permission_group.LOCATION]
 */
data class Permission(
    val label: String = "",
    val value: String,
    val type: PermissionType,
    val handler: BasePermissionHandler,
    val group: PermissionGroup = UnSpecified
)