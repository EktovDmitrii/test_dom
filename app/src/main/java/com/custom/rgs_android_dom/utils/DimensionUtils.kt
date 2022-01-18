package com.custom.rgs_android_dom.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


fun Int.pxToDp(context: Context): Int {
    return this / (context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun Int.dpToPx(context: Context): Int {
    return this * (context.resources
        .displayMetrics.densityDpi.div(DisplayMetrics.DENSITY_DEFAULT))
}

fun Int.dp(context: Context): Int {
    return this.toFloat().dp(context).toInt()
}

fun Float.dp(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

fun Float.spToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    ).toInt()
}

fun displayWidth(context: Context): Int {
    return context.resources
        .displayMetrics.widthPixels
}