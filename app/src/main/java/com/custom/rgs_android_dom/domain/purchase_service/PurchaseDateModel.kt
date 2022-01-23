package com.custom.rgs_android_dom.domain.purchase_service

import org.joda.time.LocalDateTime

data class PurchaseDateModel(
    val selectedMouth: String,
    var isPreviousMouthButtonEnable: Boolean,
    val dates: List<PurchaseDateContent>,
    var isTimesPeriodsEnable: Boolean,
    var isSelectButtonEnable: Boolean
)

data class PurchaseDateContent(
    val id: Int,
    val dayInWeek: String,
    val dateNumber: String,
    var date: LocalDateTime,
    var isEnable: Boolean,
    var isSelected: Boolean
)
