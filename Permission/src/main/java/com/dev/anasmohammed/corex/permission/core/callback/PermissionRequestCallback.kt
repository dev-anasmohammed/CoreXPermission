package com.dev.anasmohammed.corex.permission.core.callback

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.models.Permission

/**
 * Callback for [PermissionBuilder.request] method.
 */
interface PermissionRequestCallback {
    /**
     * Callback for the request result.
     * @param allGranted Indicate if all permissions that are granted.
     * @param grantedList All permissions that granted by user.
     * @param deniedList All permissions that denied by user.
     */
    fun onPermissionResult(allGranted: Boolean, grantedList: List<Permission>, deniedList: List<Permission>)
}