package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class CatalogNodeResponse(
    @SerializedName("Id")
    val id: String,

    @SerializedName("code")
    val code: String?,

    @SerializedName("iconLink")
    val iconLink: String?,

    @SerializedName("logoLarge")
    val logoLarge: String?,

    @SerializedName("logoMiddle")
    val logoMiddle: String?,

    @SerializedName("logoSmall")
    val logoSmall: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("parentNodeId")
    val parentNodeId: String?,

    @SerializedName("productTags")
    val productTags: List<String>?,

    @SerializedName("title")
    val title: String?
)