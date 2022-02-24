package com.custom.rgs_android_dom.domain.catalog.models

import com.custom.rgs_android_dom.domain.purchase.models.DeliveryType

data class ServiceShortModel(
    val priceAmount: Long?,
    val providerId: String?,
    val providerName: String?,
    var quantity: Long,
    val serviceCode: String?,
    val serviceId: String,
    val serviceName: String?,
    val serviceDeliveryType: DeliveryType,
    val serviceVersionId: String?,
    val isPurchased: Boolean = false,
    var canBeOrdered: Boolean = false
)