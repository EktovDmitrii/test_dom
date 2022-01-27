package com.custom.rgs_android_dom.domain.purchase_service.model

data class PurchasePeriodModel(
    val id: Int,
    val timesOfDay: String,
    val timeInterval: String,
    var isClickable: Boolean = true,
    var isSelected: Boolean
)
