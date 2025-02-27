package com.dev.anasmohammed.corex.permission.core.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat

/**
 * Extension function for [TextView] to set the text color.
 *
 * @param color The color resource ID to set as the text color.
 */

fun TextView.setColorOfText(@ColorRes color: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, color))
}

/**
 * Extension function for [AppCompatImageView] to set the tint color for the image.
 *
 * @param color The color resource ID to set as the image tint.
 */
fun AppCompatImageView.setColorOfImage(@ColorRes color: Int) {
    this.imageTintList = ContextCompat.getColorStateList(
        this.context, color
    )
}

/**
 * Extension function for [View] to set the background tint color.
 *
 * @param color The color resource ID to set as the background tint.
 */
fun View.setColorOfBackground(@ColorRes color: Int) {
    this.backgroundTintList = ContextCompat.getColorStateList(
        this.context, color
    )
}