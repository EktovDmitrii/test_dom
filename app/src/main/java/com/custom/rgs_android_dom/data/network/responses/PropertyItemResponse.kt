package com.custom.rgs_android_dom.data.network.responses

import com.custom.rgs_android_dom.data.network.requests.PropertyDocumentRequest
import com.google.gson.annotations.SerializedName

data class PropertyItemResponse(
    @SerializedName("address")
    val address: String,

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
    val type: String?
)

data class PropertyDocumentResponse(

    @SerializedName("link")
    val link: String?,

    @SerializedName("name")
    val name: String?

)