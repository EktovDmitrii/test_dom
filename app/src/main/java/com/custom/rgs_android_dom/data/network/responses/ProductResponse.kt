package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ProductResponse(
    @SerializedName("Code")
    val code: String?,

    @SerializedName("activatedAt")
    val activatedAt: DateTime?,

    @SerializedName("archivedAt")
    val archivedAt: DateTime?,

    @SerializedName("coolOff")
    val coolOff: ProductCoolOffResponse?,

    @SerializedName("createdAt")
    val createdAt: DateTime?,

    @SerializedName("defaultProduct")
    val defaultProduct: Boolean,

    @SerializedName("deliveryTime")
    val deliveryTime: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("descriptionFormat")
    val descriptionFormat: String?,

    @SerializedName("descriptionRef")
    val descriptionRef: String?,

    @SerializedName("duration")
    val duration: ProductDurationResponse?,

    @SerializedName("iconLink")
    val iconLink: String?,

    @SerializedName("id")
    val id: String,

    @SerializedName("insuranceProducts")
    val insuranceProducts: List<InsuranceProductResponse>?,

    @SerializedName("internalDescription")
    val internalDescription: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("objectRequired")
    val objectRequired: Boolean?,

    @SerializedName("price")
    val price: ProductPriceResponse?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("tags")
    val tags: List<String>?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("validityFrom")
    val validityFrom: DateTime?,

    @SerializedName("validityTo")
    val validityTo: DateTime?,

    @SerializedName("versionActivatedAt")
    val versionActivatedAt: DateTime?,

    @SerializedName("versionArchivedAt")
    val versionArchivedAt: DateTime?,

    @SerializedName("versionCode")
    val versionCode: String?,

    @SerializedName("versionCreatedAt")
    val versionCreatedAt: DateTime?,

    @SerializedName("versionId")
    val versionId: String?,

    @SerializedName("versionStatus")
    val versionStatus: String?
)