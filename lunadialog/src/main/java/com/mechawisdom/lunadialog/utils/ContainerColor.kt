package com.mechawisdom.lunadialog.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.mechawisdom.lunadialog.library.R

enum class ContainerColor(private val styleRes: Int) {
    BLACK(R.color.black_background_color),
    WHITE(R.color.white_background_color),
    DARK_GREY(R.color.dark_grey_background_color),
    SPACE_GREY(R.color.space_grey_background_color);

    fun COLOR(context: Context): Int {
        return ContextCompat.getColor(context, styleRes)
    }
}