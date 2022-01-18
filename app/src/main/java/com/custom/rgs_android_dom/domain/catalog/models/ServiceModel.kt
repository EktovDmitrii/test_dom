package com.custom.rgs_android_dom.domain.catalog.models

import org.joda.time.DateTime

data class ServiceModel(
    val code: String?,
    val activatedAt: DateTime?,
    val archivedAt: DateTime?,
    val createdAt: DateTime?,
    val defaultProduct: DefaultProductModel?,
    val deliveryTime: String?,
    val deliveryType: String?,
    val description: String?,
    val descriptionFormat: String?,
    val descriptionRef: String?,
    val iconLink: String?,
    val id: String,
    val internalDescription: String?,
    val name: String?,
    val objectRequired: Boolean?,
    val price: ServicePriceModel?,
    val provider: ServiceProviderModel?,
    val status: String?,
    val title: String?,
    val type: String?,
    val unitType: String?,
    val validityFrom: DateTime?,
    val validityTo: DateTime?,
    val versionActivatedAt: DateTime?,
    val versionArchivedAt: DateTime?,
    val versionCode: String?,
    val versionCreatedAt: DateTime?,
    val versionId: String?,
    val versionStatus: String?
)