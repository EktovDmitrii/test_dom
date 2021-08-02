package com.custom.rgs_android_dom.utils

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

fun String.toTimeZone(format: String = DATE_PATTERN_DATE_AND_TIME_AND_TIME_ZONE): String {
    return convertToLocalTimeZone(this, format)
}

fun String.tryParseDate(
    onError: (Exception) -> Unit = {},
    format: String = DATE_PATTERN_DATE_ONLY
): LocalDate? =
    try {
        LocalDate.parse(this, DateTimeFormat.forPattern(format))
    } catch (e: Exception) {
        onError(e)
        null
    }

