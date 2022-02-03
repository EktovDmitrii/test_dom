package com.custom.rgs_android_dom.domain.catalog.models

import org.joda.time.DateTime

data class ClientProductModel(
    val productDescriptionFormat: String?,
    val clientId: String?,
    val contractId: String?,
    val id: String,
    val objectId: String?,
    val productCode: String?,
    val productDescription: String?,
    val productDescriptionRef: String?,
    val productIcon: String?,
    val logoSmall: String?,
    val logoMiddle: String?,
    val logoLarge: String?,
    val productId: String,
    val productName: String?,
    val productTitle: String?,
    val productType: String?,
    val productVersionId: String?,
    val status: String?,
    val validityFrom: DateTime?,
    val validityTo: DateTime?
)
