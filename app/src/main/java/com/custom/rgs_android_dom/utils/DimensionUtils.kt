package com.custom.rgs_android_dom.utils

import android.content.Context
import android.util.DisplayMetrics


fun Int.pxToDp(context: Context): Int {
    return this / (context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun Int.dp(context: Context): Int {
    return this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
}