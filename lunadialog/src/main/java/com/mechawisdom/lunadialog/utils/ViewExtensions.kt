package com.mechawisdom.lunadialog.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup


fun updateLayoutParams(
    context: Context,
    view: View,
    margin: Int? = null,
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return

    margin?.let {
        val marginPx = it.dpToPx(context)
        params.setMargins(marginPx, marginPx, marginPx, marginPx)
    }

    if (left != null || top != null || right != null || bottom != null) {
        params.setMargins(
            left?.dpToPx(context) ?: params.leftMargin,
            top?.dpToPx(context) ?: params.topMargin,
            right?.dpToPx(context) ?: params.rightMargin,
            bottom?.dpToPx(context) ?: params.bottomMargin
        )
    }

    view.layoutParams = params
}

fun updateLayoutParamsSDP(
    context: Context,
    view: View,
    margin: Int? = null,
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return

    margin?.let {
        val marginPx = it.sdpToPx(context)
        params.setMargins(marginPx, marginPx, marginPx, marginPx)
    }

    if (left != null || top != null || right != null || bottom != null) {
        params.setMargins(
            left?.sdpToPx(context) ?: params.leftMargin,
            top?.sdpToPx(context) ?: params.topMargin,
            right?.sdpToPx(context) ?: params.rightMargin,
            bottom?.sdpToPx(context) ?: params.bottomMargin
        )
    }

    view.layoutParams = params
}

fun updatePadding(
    context: Context,
    view: View,
    padding: Int? = null,
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    if (padding != null) {
        val paddingPx = padding.dpToPx(context)
        view.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
    } else {
        view.setPadding(
            left?.dpToPx(context) ?: view.paddingLeft,
            top?.dpToPx(context) ?: view.paddingTop,
            right?.dpToPx(context) ?: view.paddingRight,
            bottom?.dpToPx(context) ?: view.paddingBottom
        )
    }
}

fun updatePaddingSDP(
    context: Context,
    view: View,
    padding: Int? = null,
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    if (padding != null) {
        val paddingPx = padding.sdpToPx(context)
        view.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
    } else {
        view.setPadding(
            left?.sdpToPx(context) ?: view.paddingLeft,
            top?.sdpToPx(context) ?: view.paddingTop,
            right?.sdpToPx(context) ?: view.paddingRight,
            bottom?.sdpToPx(context) ?: view.paddingBottom
        )
    }
}
