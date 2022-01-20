package com.custom.rgs_android_dom.utils

import android.text.Editable
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.regex.Pattern

private  val EMAIL_ADDRESS_PATTERN: Pattern = Pattern
    .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{0,50}" + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,29}" + "(" + "\\."
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,6}" + ")+")

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

fun String.tryParseDateTime(
    onError: (Exception) -> Unit = {},
    format: String = DATE_PATTERN_DATE_ONLY
): DateTime? =
    try {
        DateTime.parse(this, DateTimeFormat.forPattern(format))
    } catch (e: Exception) {
        onError(e)
        null
    }

fun String.tryParseLocalDateTime(
    onError: (Exception) -> Unit = {},
    format: String = DATE_PATTERN_DATE_ONLY
): LocalDateTime? =
    try {
        LocalDateTime.parse(this, DateTimeFormat.forPattern(format))
    } catch (e: Exception) {
        onError(e)
        null
    }

fun String.formatPhoneForApi(): String {
    return this.replace("+", "").replace(" ", "").replace("-", "")
}

fun String.formatPhoneByMask(mask: String, placeholder: String): String {
    var formattedPhone = mask
    this.forEach { number->
        formattedPhone = formattedPhone.replaceFirst(placeholder, number.toString(), true)
    }
    return formattedPhone
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String.isValidEmail(): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(this).matches()
}