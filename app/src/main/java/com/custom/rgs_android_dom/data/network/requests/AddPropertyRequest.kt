package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class AddPropertyRequest(

    @SerializedName("address")
    val address: PropertyAddressRequest,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("documents")
    val documents: List<PropertyDocumentRequest>?,

    @SerializedName("isOwn")
    val isOwn: String?,

    @SerializedName("isRent")
    val isRent: String?,

    @SerializedName("isTemporary")
    val isTemporary: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("totalArea")
    val totalArea: Float?,

    @SerializedName("type")
    val type: String

)

data class PropertyDocumentRequest(

    @SerializedName("link")
    val link: String?,

    @SerializedName("name")
    val name: String?

)