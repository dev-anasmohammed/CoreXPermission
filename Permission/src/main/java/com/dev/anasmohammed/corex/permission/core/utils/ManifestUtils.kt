package com.dev.anasmohammed.corex.permission.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions

/**
 * Checks if all the requested permissions are declared in the application's manifest.
 *
 * @param activity The [FragmentActivity] instance, if available.
 * @param fragment The [Fragment] instance, if available.
 * @param requestedPermissions A list of [Permission] objects representing the permissions to check.
 * @throws Exception If any of the requested permissions are not declared in the manifest.
 */
fun isPermissionsDeclaredInManifest(
    activity: FragmentActivity?,
    fragment: Fragment?,
    requestedPermissions: List<Permission>,
) {
    requestedPermissions.forEach {
        Log.e("isPermissionsDeclaredInManifest", "${it.label}")
        if (it == CoreXPermissions.Camera) {
            Log.e("isPermissionsDeclaredInManifest", "CoreXPermissions.Camera")
            checkFeatureInManifestOrThrow(activity, PackageManager.FEATURE_CAMERA)
        }
    }

    val declaredPermissions =
        getPermissionsInManifest(fragment?.requireActivity() ?: activity)

    // Find permissions that are requested but not declared in the manifest
    val undeclaredPermissions = requestedPermissions.filter { it.value !in declaredPermissions }

    if (undeclaredPermissions.isNotEmpty()) throw Exception(
        "\nSome Permissions not declared in manifest add them:\n${
            makeManifestPermissionsTags(
                undeclaredPermissions
            )
        }"
    )
}

fun isFeatureDeclaredInManifest(activity: FragmentActivity?, featureName: String): Boolean {
    try {
        val packageInfo = activity?.packageManager?.getPackageInfo(
            activity.packageName!!,
            PackageManager.GET_CONFIGURATIONS
        )

        val usesFeatures = packageInfo!!.reqFeatures ?: return false
        for (feature in usesFeatures) {
            if (feature.name == featureName) {
                return true
            }
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return false
}

fun checkFeatureInManifestOrThrow(activity: FragmentActivity?, featureName: String) {
    if (!isFeatureDeclaredInManifest(activity, featureName)) {
        throw Exception(
            "\nThe hardware feature $featureName is not declared in the manifest!\n" +
                    "<uses-feature\n" +
                    "        android:name=\"$featureName\"\n" +
                    "        android:required=\"false\" />"
        )
    }
}

/**
 * Retrieves the list of permissions declared in the application's manifest.
 *
 * @param fragmentActivity The [FragmentActivity] instance to get the permissions from.
 * @return A list of strings representing the declared permissions.
 */
private fun getPermissionsInManifest(
    fragmentActivity: FragmentActivity?,
): List<String> {
    if (fragmentActivity == null) return emptyList()

    // Get the list of all declared permissions
    val packageInfo = fragmentActivity.packageManager.getPackageInfo(
        fragmentActivity.packageName, PackageManager.GET_PERMISSIONS
    )
    val declaredPermissions = packageInfo.requestedPermissions ?: emptyArray()

    return declaredPermissions.toList()
}

/**
 * Generates the `<uses-permission>` tags for the specified permissions.
 *
 * @param permissions A list of [Permission] objects representing the permissions to generate tags for.
 * @return A string containing the `<uses-permission>` tags for the permissions.
 */
private fun makeManifestPermissionsTags(permissions: List<Permission>): String {
    var tags = ""
    permissions.forEach { permission ->
        tags += "<uses-permission android:name=\"${permission.value}\"/>\n"
    }
    return tags
}