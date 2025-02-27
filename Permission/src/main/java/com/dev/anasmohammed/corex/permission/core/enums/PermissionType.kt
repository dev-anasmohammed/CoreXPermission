package com.dev.anasmohammed.corex.permission.core.enums

import com.dev.anasmohammed.corex.permission.core.utils.requestMultiNormalPermissions
/**
 * Types of permission that library handles
 * @see Normal permission that can be request as a runtime permission without any custom implementation
 * by call [requestMultiNormalPermissions]
 *
 * @see Special permissions that we need to custom implementation by going to setting or special check
 */
sealed class PermissionType(private val value : String) {
    data object Normal : PermissionType("normal")
    data object Special : PermissionType("special")
}