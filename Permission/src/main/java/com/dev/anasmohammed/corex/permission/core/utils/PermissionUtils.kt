package com.dev.anasmohammed.corex.permission.core.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * A helper function to check a permission is granted or not.
 *
 * @param context Any context, will not be retained.
 * @param permission Specific permission name to check. e.g. [android.Manifest.permission.CAMERA].
 * @return True if this permission is granted, False otherwise.
 */
fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

