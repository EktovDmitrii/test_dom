package com.custom.rgs_android_dom.utils

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

const val DATE_PATTERN_DATE_ONLY = "dd.MM.yyyy"
const val DATE_PATTERN_DAY_FULL_ONLY = "dd.MMM"
const val DATE_PATTERN_DAY_MONTH_FULL_ONLY = "dd MMMM"
const val DATE_PATTERN_DATE_AND_TIME = "dd.MM.yyyy HH:mm:ss"
const val DATE_PATTERN_TIME_ONLY_WITHOUT_SEC = "HH:mm"
const val DATE_PATTERN_DATE_AND_TIME_AND_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZZ"
const val PATTERN_DATE_TIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun LocalDate.formatTo(pattern: String = DATE_PATTERN_DATE_ONLY): String {
    return toString(DateTimeFormat.forPattern(pattern))
}

fun LocalTime.formatTo(pattern: String = DATE_PATTERN_TIME_ONLY_WITHOUT_SEC): String {
    return toString(DateTimeFormat.forPattern(pattern))
}

fun LocalDateTime.formatTo(pattern: String = DATE_PATTERN_DATE_AND_TIME): String {
    return toString(DateTimeFormat.forPattern(pattern))
}

fun DateTime.formatTo(pattern: String = DATE_PATTERN_DATE_AND_TIME): String {
    return toString(DateTimeFormat.forPattern(pattern))
}

fun LocalDateTime.getPeriod(comparedDate: LocalDateTime): Period {
    return Period.fieldDifference(this, comparedDate)
}
