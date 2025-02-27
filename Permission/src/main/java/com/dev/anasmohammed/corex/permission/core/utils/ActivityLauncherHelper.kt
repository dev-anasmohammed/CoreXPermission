package com.dev.anasmohammed.corex.permission.core.utils

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dev.anasmohammed.corex.permission.core.enums.PermissionType
import com.dev.anasmohammed.corex.permission.core.handlers.NormalPermissionsHandler
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.data.CoreXPermissionGroups.UnSpecified

/**
 * Extension function for [Fragment] to start an activity for result.
 *
 * Registers an activity result launcher that starts an activity and handles its result.
 * The provided `postForResult` callback is invoked with the [ActivityResult] once the activity finishes.
 *
 * @param key A unique key to register this result.
 * @param postForResult A callback to handle the result when the activity finishes.
 * @return An [ActivityResultLauncher] for starting the activity.
 */
fun Fragment.startActivityForResult(
    key: String, postForResult: (ActivityResult) -> Unit
): ActivityResultLauncher<Intent> {
    return requireActivity().activityResultRegistry.register(
        key, ActivityResultContracts.StartActivityForResult()
    ) { result ->
        postForResult(result)
    }
}

/**
 * Extension function for [Fragment] to request a single permission.
 *
 * Registers an activity result launcher that requests a single permission from the user.
 * The provided `postForResult` callback is invoked with the result of the permission request.
 *
 * @param key A unique key to register this result.
 * @param postForResult A callback to handle whether the permission was granted or denied.
 */
fun Fragment.requestPermission(
    key: String, postForResult: (Boolean) -> Unit
) = requireActivity().activityResultRegistry.register(
    key, ActivityResultContracts.RequestPermission()
) { result ->
    postForResult.invoke(result)
}


/**
 * Extension function for [Fragment] to request multiple permissions at once.
 *
 * Registers an activity result launcher that requests multiple permissions from the user.
 * The provided `postForResult` callback is invoked with the result for each permission requested.
 *
 * @param key A unique key to register this result.
 * @param permissions A set of [Permission] objects representing the permissions to request.
 * @param postForResult A callback to handle the results of the permission requests.
 */
fun Fragment.requestMultiNormalPermissions(
    key: String,
    permissions: Set<Permission>,
    postForResult: ((Map<Permission, Boolean>) -> Unit)? = null
) = requireActivity().activityResultRegistry.register(
    key, ActivityResultContracts.RequestMultiplePermissions()
) { result ->
    val permissionResult: Map<Permission, Boolean> = result.map { (key, value) ->
        val matchingPermission = permissions.find { it.value == key }
        Permission(
            label = matchingPermission?.label ?: "",
            value = key,
            type = PermissionType.Normal,
            handler = NormalPermissionsHandler(),
            group = matchingPermission?.group ?: UnSpecified
        ) to value
    }.toMap()

    postForResult?.invoke(permissionResult)
}