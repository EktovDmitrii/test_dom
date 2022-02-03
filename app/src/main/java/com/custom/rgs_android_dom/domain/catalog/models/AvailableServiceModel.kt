package com.custom.rgs_android_dom.domain.catalog.models

import org.joda.time.DateTime

data class AvailableServiceModel(
    val id: String,
    val serviceId: String,
    val productId: String,
    val serviceName: String,
    val productIcon: String,
    val available: Int,
    val objectIds: List<String>?,
    val validityFrom: DateTime?,
    val validityTo: DateTime?,
    val total: Int
)