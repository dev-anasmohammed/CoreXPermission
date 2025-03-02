package com.dev.anasmohammed.corex.permission.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dev.anasmohammed.corex.permission.core.enums.PermissionType
import com.dev.anasmohammed.corex.permission.core.handlers.base.BaseSpecialPermissionHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.core.utils.isPermissionsDeclaredInManifest
import com.dev.anasmohammed.corex.permission.core.utils.logMe
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions
import com.dev.anasmohammed.corex.permission.data.PermissionCategory

/**
 * This class used to parse permissions to [PermissionBuilder] to request permissions.
 * Its responsible to sort and filter requested permission between normal or special or remove it.
 */
class PermissionMediator(
    private val activity: FragmentActivity? = null,
    private val fragment: Fragment? = null,
) {

    /**
     * Holds the permission category that user want to request
     */
    internal var permissionCategory: PermissionCategory? = null

    /**
     * Holds the normal permissions that will be requested [PermissionType.Normal]
     */
    internal val normalPermissions = LinkedHashSet<Permission>()

    /**
     * Holds the special permissions that will be requested [PermissionType.Special]
     */
    internal val specialPermissions = LinkedHashSet<Permission>()

    /**
     * Holds the special permissions that will be requested [PermissionType.Special]
     */
    internal var canCheckPermissionInManifest = true

    /**
     * Set of permissions for specific feature.
     *
     * @param category A type from enum class [PermissionCategory].
     * @return PermissionBuilder itself.
     */
    fun permissionCategory(
        category: PermissionCategory,
        canCheckPermissionsInManifest: Boolean = true
    ): PermissionBuilder {
        canCheckPermissionInManifest = canCheckPermissionsInManifest
        permissionCategory = category
        return setRequestPermissions(category.permissions.toMutableList(), category)
    }

    /**
     * All permissions that you want to request.
     *
     * @param permissions A vararg param to pass permissions [CoreXPermissions].
     * @return PermissionBuilder itself.
     */
    fun permissions(vararg permissions: Permission): PermissionBuilder {
        return setRequestPermissions(mutableListOf(*permissions))
    }

    /**
     * All permissions that you want to request.
     *
     * @param permissions A vararg param to pass permissions [CoreXPermissions].
     * @return PermissionBuilder itself.
     */
    fun permissions(
        permissions: List<Permission>,
        canCheckPermissionsInManifest: Boolean = true
    ): PermissionBuilder {
        canCheckPermissionInManifest = canCheckPermissionsInManifest
        return setRequestPermissions(permissions.toMutableList())
    }

    /**
     * All permissions that you want to request.
     *
     * @param permissions A vararg param to pass permissions [CoreXPermissions]..
     * @param permissionCategory if have category get its configuration [PermissionCategory].
     * @return PermissionBuilder itself.
     */
    private fun setRequestPermissions(
        permissions: MutableList<Permission>,
        permissionCategory: PermissionCategory = PermissionCategory.None
    ): PermissionBuilder {

        //remove ignore battery optimization if enabled set to false
        if (permissionCategory is PermissionCategory.Location.HighAccurateTracking && !permissionCategory.isBatteryOptimizeEnabled) {
            permissions.removeIf { it.value == CoreXPermissions.RequestIgnoreBatteryOptimization.value }
        }

        //manifest check
        if (canCheckPermissionInManifest) {
            isPermissionsDeclaredInManifest(activity, fragment, permissions)
        }

        logMe("PermissionMediator permissions\n")
        permissions.forEach {
            logMe("${it.value}\n")
        }
        logMe("--------------\n")

        //sort permissions to normal or special
        permissions.forEach { permission ->
            when (permission.type) {
                PermissionType.Normal -> normalPermissions.add(permission)
                PermissionType.Special -> specialPermissions.add(permission)
            }
        }

        //resort permissions based on the sdk version add or remove it from normal or special
        val tempSpecialPermissionSet = specialPermissions.toMutableSet()
        tempSpecialPermissionSet.forEach { permission ->
            (permission.handler as BaseSpecialPermissionHandler).adjustPermissionsForSdk(this@PermissionMediator)
        }

        logMe("PermissionMediator normalPermissionSet\n")
        normalPermissions.forEach {
            logMe("${it.value}\n")
        }
        logMe("--------------\n")

        logMe("PermissionMediator specialPermissions\n")
        specialPermissions.forEach {
            logMe("${it.value}\n")
        }

        return PermissionBuilder(
            activity, fragment, normalPermissions, specialPermissions, this@PermissionMediator
        )
    }
}