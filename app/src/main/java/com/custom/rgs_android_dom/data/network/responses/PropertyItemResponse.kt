package com.custom.rgs_android_dom.data.network.responses

import com.custom.rgs_android_dom.data.network.requests.PropertyDocumentRequest
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.google.gson.annotations.SerializedName

data class PropertyItemResponse(
    @SerializedName("address")
    val address: PropertyAddressResponse?,

    @SerializedName("clientId")
    val clientId: String,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("documents")
    val documents: List<PropertyDocumentResponse>?,

    @SerializedName("id")
    val id: String,

    @SerializedName("isOwn")
    val isOwn: String?,

    @SerializedName("isRent")
    val isRent: String?,

    @SerializedName("isTemporary")
    val isTemporary: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("status")
    val status: String?,

    @SerializedName("totalArea")
    val totalArea: Float?,

    @SerializedName("type")
    val type: PropertyType
)

data class PropertyDocumentResponse(

    @SerializedName("link")
    val link: String?,

    @SerializedName("name")
    val name: String?

)

data class PropertyAddressResponse(
    @SerializedName("address")
    val address: String?,

    @SerializedName("cityFiasId")
    val cityFiasId: String?,

    @SerializedName("cityName")
    val cityName: String?,

    @SerializedName("fiasId")
    val fiasId: String?,

    @SerializedName("regionFiasId")
    val regionFiasId: String?,

    @SerializedName("regionName")
    val regionName: String?
)