package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdatePropertyRequest (

    @SerializedName("address")
    val address: PropertyAddressRequest,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("documents")
    val documents: List<PropertyDocumentRequest>?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("isOwn")
    val isOwn: String?,

    @SerializedName("isRent")
    val isRent: String?,

    @SerializedName("isTemporary")
    val isTemporary: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("totalArea")
    val totalArea: Float?
)
