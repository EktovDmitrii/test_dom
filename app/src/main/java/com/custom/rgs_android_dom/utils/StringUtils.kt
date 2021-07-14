package com.custom.rgs_android_dom.utils

fun String.toTimeZone(format: String = DATE_PATTERN_DATE_AND_TIME_AND_TIME_ZONE): String {
    return convertToLocalTimeZone(this, format)
}

