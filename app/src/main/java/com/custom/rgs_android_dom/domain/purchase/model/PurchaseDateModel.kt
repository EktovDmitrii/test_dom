package com.custom.rgs_android_dom.domain.purchase.model

import org.joda.time.LocalDateTime

data class PurchaseDateModel(
    val date: LocalDateTime = LocalDateTime.now(),
    val selectedMonth: String,
    var isPreviousMonthButtonEnable: Boolean,
    val datesForCalendar: List<DateForCalendarModel>,
    var periodList: List<PurchaseTimePeriodModel>,
    var selectedPeriod: PurchaseTimePeriodModel? = null
)
