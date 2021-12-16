package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ServiceResponse(
    @SerializedName("Code")
    val code: String?,

    @SerializedName("activatedAt")
    val activatedAt: DateTime?,

    @SerializedName("archivedAt")
    val archivedAt: DateTime?,

    @SerializedName("createdAt")
    val createdAt: DateTime?,

    @SerializedName("defaultProduct")
    val defaultProduct: DefaultProductResponse?,

    @SerializedName("deliveryTime")
    val deliveryTime: String?,

    @SerializedName("deliveryType")
    val deliveryType: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("descriptionFormat")
    val descriptionFormat: String?,

    @SerializedName("descriptionRef")
    val descriptionRef: String?,

    @SerializedName("iconLink")
    val iconLink: String?,

    @SerializedName("id")
    val id: String,

    @SerializedName("internalDescription")
    val internalDescription: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("objectRequired")
    val objectRequired: Boolean?,

    @SerializedName("price")
    val price: ServicePriceResponse?,

    @SerializedName("provider")
    val provider: ServiceProviderResponse?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("unitType")
    val unitType: String?,

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