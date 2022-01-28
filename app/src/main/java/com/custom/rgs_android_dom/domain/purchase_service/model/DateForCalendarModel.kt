package com.custom.rgs_android_dom.domain.purchase_service.model

import org.joda.time.LocalDateTime

data class DateForCalendarModel(
    val dayInWeek: String,
    val dateNumber: String,
    var date: LocalDateTime,
    var isEnable: Boolean,
    var isSelected: Boolean
)
