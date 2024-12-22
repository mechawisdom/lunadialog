package com.mechawisdom.lunadialog.utils

import android.content.Context
import android.util.TypedValue


fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun Int.sdpToPx(context: Context): Int {
    return context.resources.getDimension(this).toInt()
}