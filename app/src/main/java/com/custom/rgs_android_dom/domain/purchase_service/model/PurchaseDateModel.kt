package com.custom.rgs_android_dom.domain.purchase_service.model

import org.joda.time.LocalDateTime

data class PurchaseDateModel(
    val date: LocalDateTime = LocalDateTime.now(),
    val selectedMouth: String,
    var isPreviousMouthButtonEnable: Boolean,
    val datesForCalendar: List<DateForCalendarModel>,
    var isSelectButtonEnable: Boolean,
    var periodList: List<PurchasePeriodModel>
)
