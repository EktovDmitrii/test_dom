package com.custom.rgs_android_dom.domain.catalog.models

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDateTime

data class ProductModel(
    val code: String?,
    val activatedAt: LocalDateTime?,
    val archivedAt: LocalDateTime?,
    val coolOff: ProductCoolOffModel?,
    val createdAt: LocalDateTime?,
    val defaultProduct: Boolean,
    val deliveryTime: LocalDateTime?,
    val description: String?,
    val descriptionFormat: String,
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
    val validityFrom: LocalDateTime?,
    val validityTo: LocalDateTime?,
    val versionActivatedAt: LocalDateTime?,
    val versionArchivedAt: LocalDateTime?,
    val versionCode: String,
    val versionCreatedAt: LocalDateTime?,
    val versionId: String,
    val versionStatus: String?
)