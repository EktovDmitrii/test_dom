package com.custom.rgs_android_dom.domain.catalog.models

import org.joda.time.DateTime

data class AvailableServiceModel(
    val id: String,
    val serviceId: String,
    val productId: String,
    val clientProductId: String,
    var productVersionId: String,
    val serviceVersionId: String,
    val serviceName: String,
    val productIcon: String,
    val serviceIcon: String,
    val available: Int,
    val objectId: String?,
    val validityFrom: DateTime?,
    val validityTo: DateTime?,
    val canBeOrdered: Boolean = false,
    val total: Int
)