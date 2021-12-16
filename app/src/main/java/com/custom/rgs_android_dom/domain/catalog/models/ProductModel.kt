package com.custom.rgs_android_dom.domain.catalog.models

import org.joda.time.DateTime

data class ProductModel(
    val code: String?,
    val activatedAt: DateTime?,
    val archivedAt: DateTime?,
    val coolOff: ProductCoolOffModel?,
    val createdAt: DateTime?,
    val defaultProduct: Boolean,
    val deliveryTime: String?,
    val description: String?,
    val descriptionFormat: String?,
    val descriptionRef: String?,
    val duration: ProductDurationModel?,
    val iconLink: String?,
    val id: String,
    val insuranceProducts: List<InsuranceProductModel>?,
    val internalDescription: String?,
    val name: String,
    val objectRequired: Boolean?,
    val price: ProductPriceModel?,
    val status: String?,
    val tags: List<String>?,
    val title: String?,
    val type: String?,
    val validityFrom: DateTime?,
    val validityTo: DateTime?,
    val versionActivatedAt: DateTime?,
    val versionArchivedAt: DateTime?,
    val versionCode: String?,
    val versionCreatedAt: DateTime?,
    val versionId: String?,
    val versionStatus: String?
)