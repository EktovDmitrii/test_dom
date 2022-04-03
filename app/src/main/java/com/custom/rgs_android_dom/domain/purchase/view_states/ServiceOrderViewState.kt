package com.custom.rgs_android_dom.domain.purchase.view_states

import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel

data class ServiceOrderViewState(
    val property: PropertyItemModel? = null,
    val comment: String? = null,
    val orderDate: PurchaseDateTimeModel? = null,
    val isServiceOrderTextViewEnabled: Boolean = false
)