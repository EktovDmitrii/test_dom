package com.custom.rgs_android_dom.utils

import java.text.SimpleDateFormat
import java.util.*

fun convertToLocalTimeZone(date: String, format: String): String {
    val formatter = SimpleDateFormat(format)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    val value: Date = formatter.parse(date)
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(value)
}