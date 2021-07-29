package com.custom.rgs_android_dom.utils

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.util.*

fun getAge(birthDate: LocalDateTime): Int {
    val today = LocalDateTime.now()
    var age: Int = today.year - birthDate.year
    if (today.dayOfYear < birthDate.dayOfYear) {
        age--
    }
    return age
}

fun getAge(birthDate: LocalDate): Int {
    val today = LocalDate.now()
    var age: Int = today.year - birthDate.year
    if (today.dayOfYear < birthDate.dayOfYear) {
        age--
    }
    return age
}

fun getMinBirthdayDateByAge(age: Int): Date {
    return LocalDate.now().minusYears(age).plusDays(1).toDate()
}