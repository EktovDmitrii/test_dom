package com.custom.rgs_android_dom.domain.catalog.models

data class AvailableServiceModel(
    val id: String,
    val serviceId: String,
    val productId: String,
    val serviceName: String,
    val productIcon: String,
    val available: Int,
    val total: Int
)