package com.custom.rgs_android_dom.domain.catalog.models

data class ServiceShortModel(
    val priceAmount: Long?,
    val providerId: String?,
    val providerName: String?,
    var quantity: Long,
    val serviceCode: String?,
    val serviceId: String,
    val serviceName: String?,
    val serviceVersionId: String?,
    val isPurchased: Boolean = false
)