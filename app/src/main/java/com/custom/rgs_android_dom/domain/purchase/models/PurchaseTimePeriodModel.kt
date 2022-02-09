package com.custom.rgs_android_dom.domain.purchase.models

data class PurchaseTimePeriodModel(
    val id: Int,
    val timeOfDay: String,
    val timeFrom: String,
    val timeTo: String,
    var isSelectable: Boolean = true,
    var isSelected: Boolean = false
)
