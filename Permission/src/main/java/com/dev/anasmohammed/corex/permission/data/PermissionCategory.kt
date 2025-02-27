package com.dev.anasmohammed.corex.permission.data

import android.annotation.SuppressLint
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.core.models.Permission

/**
 * A sealed class representing different categories of permissions that can be requested by the app.
 * Developers can use these categories to request permissions without needing to specify each individual permission
 * or what is the permission to do this functionality.
 * Some categories include subcategories (e.g., Location) to specify different levels of permission (e.g., low accuracy, high accuracy).
 * Also some of them has configuration inside it that is helpful in requesting the permission
 *
 * @param permissions A list of permissions related to this category.
 * @param rationaleMessage The message shown in the rationale dialog.
 * @param rationalePositiveText The text for the positive button in the rationale dialog.
 * @param rationaleNegativeText The text for the negative button in the rationale dialog.
 * @param settingMessage The message shown in the settings dialog.
 * @param settingPositiveText The text for the positive button in the settings dialog.
 * @param settingNegativeText The text for the negative button in the settings dialog.
 */

@SuppressLint("InlinedApi")
sealed class PermissionCategory(
    val permissions: List<Permission>,
    val rationaleMessage: Int,
    val rationalePositiveText: Int,
    val rationaleNegativeText: Int,
    val settingMessage: Int,
    val settingPositiveText: Int,
    val settingNegativeText: Int
) {
    /** None **/
    data object None : PermissionCategory(
        permissions = listOf(),
        rationaleMessage = R.string.app_name,
        rationalePositiveText = R.string.app_name,
        rationaleNegativeText = R.string.app_name,
        settingMessage = R.string.app_name,
        settingPositiveText = R.string.app_name,
        settingNegativeText = R.string.app_name
    )

    /** Notification **/
    data object Notification : PermissionCategory(
        permissions = listOf(CoreXPermissions.Notification),
        rationaleMessage = R.string.core_x_permission_notification_rationale,
        rationalePositiveText = R.string.core_x_permission_notification_rationale_positive_button,
        rationaleNegativeText = R.string.core_x_permission_notification_rationale_negative_button,
        settingMessage = R.string.core_x_permission_notification_settings,
        settingPositiveText = R.string.core_x_permission_notification_settings_positive_button,
        settingNegativeText = R.string.core_x_permission_notification_settings_negative_button
    )

    /** Location **/
    sealed class Location(
        permissions: List<Permission>,
        rationaleMessage: Int,
        rationalePositiveText: Int,
        rationaleNegativeText: Int,
        settingMessage: Int,
        settingPositiveText: Int,
        settingNegativeText: Int
    ) : PermissionCategory(
        permissions,
        rationaleMessage,
        rationalePositiveText,
        rationaleNegativeText,
        settingMessage,
        settingPositiveText,
        settingNegativeText
    ) {
        data object Standard : Location(
            permissions = listOf(
                CoreXPermissions.FineLocation,
                CoreXPermissions.CoarseLocation
            ),
            rationaleMessage = R.string.core_x_permission_location_standard_rationale,
            rationalePositiveText = R.string.core_x_permission_location_standard_rationale_positive_button,
            rationaleNegativeText = R.string.core_x_permission_location_standard_rationale_negative_button,
            settingMessage = R.string.core_x_permission_location_standard_settings,
            settingPositiveText = R.string.core_x_permission_location_standard_settings_positive_button,
            settingNegativeText = R.string.core_x_permission_location_standard_settings_negative_button
        )

        data object LowAccurateTracking : Location(
            permissions = listOf(
                CoreXPermissions.Notification, // API 33 Android 13.0
                CoreXPermissions.FineLocation,
                CoreXPermissions.CoarseLocation
            ),
            rationaleMessage = R.string.core_x_permission_location_low_accuracy_rationale,
            rationalePositiveText = R.string.core_x_permission_location_low_accuracy_rationale_positive_button,
            rationaleNegativeText = R.string.core_x_permission_location_low_accuracy_rationale_negative_button,
            settingMessage = R.string.core_x_permission_location_low_accuracy_settings,
            settingPositiveText = R.string.core_x_permission_location_low_accuracy_settings_positive_button,
            settingNegativeText = R.string.core_x_permission_location_low_accuracy_settings_negative_button
        )

        data class HighAccurateTracking(val isBatteryOptimizeEnabled: Boolean) : Location(
            permissions = listOf(
                CoreXPermissions.ForegroundService, // API 28 Android 9.0
                CoreXPermissions.ForegroundServiceLocation, // API 34 Android 14.0
                CoreXPermissions.Notification, // API 33 Android 13.0
                CoreXPermissions.CoarseLocation, // API 1 Android 1.0
                CoreXPermissions.FineLocation, // API 1 Android 1.0
                CoreXPermissions.BackgroundLocation, // API 29 Android 10.0
                CoreXPermissions.RequestIgnoreBatteryOptimization // API 23 Android 6.0
            ),
            rationaleMessage = R.string.core_x_permission_location_high_accuracy_rationale,
            rationalePositiveText = R.string.core_x_permission_location_high_accuracy_rationale_positive_button,
            rationaleNegativeText = R.string.core_x_permission_location_high_accuracy_rationale_negative_button,
            settingMessage = R.string.core_x_permission_location_high_accuracy_settings,
            settingPositiveText = R.string.core_x_permission_location_high_accuracy_settings_positive_button,
            settingNegativeText = R.string.core_x_permission_location_high_accuracy_settings_negative_button
        )
    }
}