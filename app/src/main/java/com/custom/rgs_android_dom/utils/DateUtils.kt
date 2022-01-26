package com.custom.rgs_android_dom.utils

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

const val DATE_PATTERN_DATE_ONLY = "dd.MM.yyyy"
const val DATE_PATTERN_DATE_FULL_MONTH = "dd MMMM yyyy"
const val DATE_PATTERN_DAY_OF_WEEK = "EE"
const val DATE_PATTERN_MONTH_FULL_ONLY = "dd MMM"
const val DATE_PATTERN_MONTH_FULL_AND_TIME = "dd MMM в HH:mm"
const val DATE_PATTERN_YEAR_MONTH_FULL_AND_TIME = "dd MMM yyyy в HH:mm"
const val DATE_PATTERN_YEAR_MONTH_FULL_ONLY = "dd MMM yyyy"
const val DATE_PATTERN_DAY_MONTH_FULL_ONLY = "dd MMMM"
const val DATE_PATTERN_YEAR_MONTH = "MMMM yyyy"
const val DATE_PATTERN_DATE_AND_TIME = "dd.MM.yyyy HH:mm:ss"
const val DATE_PATTERN_TIME_ONLY_WITHOUT_SEC = "HH:mm"
const val DATE_PATTERN_DATE_AND_TIME_AND_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZZ"
const val DATE_PATTERN_DATE_AND_TIME_FOR_PURCHASE = "yyyy-MM-dd'T'HH:mm:ss"
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

fun LocalDateTime.toHumanReadableDate(showTime: Boolean = true): String {

    val hoursPattern = DateTimeFormat.forPattern(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

    val today = LocalDateTime.now()

    return when {
        today.toLocalDate().compareTo(this.toLocalDate()) == 0 -> {
            if (showTime) {
                String.format("Сегодня в %s", hoursPattern.print(this))
            } else {
                "Сегодня"
            }
        }

        today.minusDays(1).toLocalDate().compareTo(this.toLocalDate()) == 0 -> { //Вчера
            if (showTime) {
                String.format("Вчера в %s", hoursPattern.print(this))
            } else {
                "Вчера"
            }
        }

        else -> {
            if (showTime) {
                val pattern = if (today.year == this.year) DATE_PATTERN_MONTH_FULL_AND_TIME else DATE_PATTERN_YEAR_MONTH_FULL_AND_TIME
                this.toString(DateTimeFormat.forPattern(pattern))
            } else {
                val pattern = if (today.year == this.year) DATE_PATTERN_MONTH_FULL_ONLY else DATE_PATTERN_YEAR_MONTH_FULL_ONLY
                this.toString(DateTimeFormat.forPattern(pattern))
            }
        }
    }
}

fun Duration.toReadableTime(): String {
    val hours = standardSeconds / 3600
    val minutes = (standardSeconds % 3600) / 60
    val seconds = standardSeconds % 60

    return if (hours >0 ) String.format("%02d:%02d:%02d", hours, minutes, seconds) else String.format("%02d:%02d", minutes, seconds)
}
