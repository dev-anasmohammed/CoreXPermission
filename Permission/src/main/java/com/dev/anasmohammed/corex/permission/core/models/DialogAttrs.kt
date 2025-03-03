package com.dev.anasmohammed.corex.permission.core.models

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dev.anasmohammed.corex.permission.R
import com.dev.anasmohammed.corex.permission.presentation.dialog.CoreXDefaultDialog

/**
 * A data class representing the attributes of [CoreXDefaultDialog].
 * This class provides default values for all customizable properties,
 * allowing you to easily configure the appearance and behavior of the dialog.
 *
 * @param showIcon Indicates whether the dialog should display an icon. (Default: `true`)
 * @param icon Drawable resource for the dialog's main icon. (Default: `R.drawable.ic_cxp_default_icon`)
 * @param settingsIcon Drawable resource for the settings icon. (Default: `R.drawable.ic_cxp_settings`)
 * @param title String resource for the dialog's title. (Default: `R.string.cxp_default_title`)
 * @param settingsTitle String resource for the settings dialog title. (Default: `R.string.cxp_default_title_settings`)
 * @param message String resource for the dialog's message. (Default: `R.string.cxp_default_message`)
 * @param positiveText String resource for the positive button text. (Default: `R.string.cxp_default_positive_button`)
 * @param negativeText String resource for the negative button text. (Default: `R.string.cxp_default_negative_button`)
 * @param iconColor Color resource for the icon. (Default: `R.color.cxp_default_dialog_icon_color`)
 * @param iconBackgroundColor Color resource for the icon's background. (Default: `R.color.cxp_default_dialog_icon_background_color`)
 * @param titleTextColor Color resource for the title text. (Default: `R.color.cxp_default_dialog_title_color`)
 * @param messageColor Color resource for the dialog message text. (Default: `R.color.cxp_default_dialog_message_color`)
 * @param positiveTextColor Color resource for the positive button text. (Default: `R.color.cxp_default_dialog_positive_text_color`)
 * @param positiveButtonColor Color resource for the positive button's background. (Default: `R.color.cxp_default_dialog_positive_button_color`)
 * @param negativeTextColor Color resource for the negative button text. (Default: `R.color.cxp_default_dialog_negative_text_color`)
 * @param negativeButtonColor Color resource for the negative button's background. (Default: `R.color.cxp_default_dialog_negative_button_color`)
 * @param itemTextColor Color resource for the text color of list items in the dialog. (Default: `R.color.cxp_default_dialog_item_text_color`)
 * @param itemIconColor Color resource for the icons of list items in the dialog. (Default: `R.color.cxp_default_dialog_item_icon_color`)
 * @param dialogRadius Dimension resource for the corner radius of the dialog. (Default: `R.dimen.dimen_016dp`)
 * @param dialogBackgroundColor Color resource for the dialog's background. (Default: `R.color.cxp_default_dialog_background_color`)
 * @param dialogButtonRadius Dimension resource for the corner radius of the dialog buttons. (Default: `R.dimen.dimen_016dp`)
 * @param dialogStrokeColor Color resource for the stroke/border of the dialog. (Default: `R.color.cxp_default_dialog_stroke_color`)
 * @param dialogStrokeWidth Dimension resource for the width of the dialog's stroke/border. (Default: `R.dimen.dimen_000dp`)
 */
data class DialogAttrs(
    var showIcon: Boolean = true,
    @DrawableRes var icon: Int = R.drawable.ic_cxp_default_icon,
    @DrawableRes var settingsIcon: Int = R.drawable.ic_cxp_settings,
    @StringRes var title: Int = R.string.cxp_default_title,
    @StringRes var settingsTitle: Int = R.string.cxp_default_title_settings,
    @StringRes var message: Int = R.string.cxp_default_message,
    @StringRes var positiveText: Int = R.string.cxp_default_positive_button,
    @StringRes var negativeText: Int = R.string.cxp_default_negative_button,
    @ColorRes var iconColor: Int = R.color.cxp_default_dialog_icon_color,
    @ColorRes var iconBackgroundColor: Int = R.color.cxp_default_dialog_icon_background_color,
    @ColorRes var titleTextColor: Int = R.color.cxp_default_dialog_title_color,
    @ColorRes var messageColor: Int = R.color.cxp_default_dialog_message_color,
    @ColorRes var positiveTextColor: Int = R.color.cxp_default_dialog_positive_text_color,
    @ColorRes var positiveButtonColor: Int = R.color.cxp_default_dialog_positive_button_color,
    @ColorRes var negativeTextColor: Int = R.color.cxp_default_dialog_negative_text_color,
    @ColorRes var negativeButtonColor: Int = R.color.cxp_default_dialog_negative_button_color,
    @ColorRes var itemTextColor: Int = R.color.cxp_default_dialog_item_text_color,
    @ColorRes var itemIconColor: Int = R.color.cxp_default_dialog_item_icon_color,
    @DimenRes var dialogRadius : Int = R.dimen.dimen_016dp,
    @ColorRes var dialogBackgroundColor : Int = R.color.cxp_default_dialog_background_color,
    @DimenRes var dialogButtonRadius : Int = R.dimen.dimen_016dp,
    @ColorRes var dialogStrokeColor : Int = R.color.cxp_default_dialog_stroke_color,
    @DimenRes var dialogStrokeWidth : Int = R.dimen.dimen_000dp,
)