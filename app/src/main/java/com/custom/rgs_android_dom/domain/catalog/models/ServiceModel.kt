package com.custom.rgs_android_dom.domain.catalog.models

import com.custom.rgs_android_dom.domain.purchase.models.DeliveryType
import org.joda.time.DateTime

data class ServiceModel(
    val code: String?,
    val activatedAt: DateTime?,
    val archivedAt: DateTime?,
    val createdAt: DateTime?,
    val defaultProduct: DefaultProductModel?,
    val deliveryTime: String?,
    val deliveryType: DeliveryType?,
    val description: String?,
    val descriptionFormat: String?,
    val descriptionRef: String?,
    val logoSmall: String,
    val logoMiddle: String,
    val logoLarge: String,
    val iconLink: String,
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
    val duration: ServiceDurationModel?,
    val validityFrom: DateTime?,
    val validityTo: DateTime?,
    val versionActivatedAt: DateTime?,
    val versionArchivedAt: DateTime?,
    val versionCode: String?,
    val versionCreatedAt: DateTime?,
    val versionId: String?,
    val versionStatus: String?
)