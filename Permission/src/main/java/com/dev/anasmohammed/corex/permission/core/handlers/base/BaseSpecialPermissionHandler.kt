package com.dev.anasmohammed.corex.permission.core.handlers.base

import com.dev.anasmohammed.corex.permission.core.PermissionMediator

/**
 * Special BasePermissionsHandler for special permissions
 */
abstract class BaseSpecialPermissionHandler : BasePermissionHandler() {
    /**
     * Used in [PermissionMediator] to sort special permission to check if it will be requested as
     * normal or special permission depended on Android Version or another factors
     * Or it removed from the normal and special permissions
     */
    abstract fun adjustPermissionsForSdk(permissionMediator: PermissionMediator)
}