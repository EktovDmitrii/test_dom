package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class CatalogNodeResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("code")
    val code: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("logoLarge")
    val logoLarge: String?,

    @SerializedName("logoMiddle")
    val logoMiddle: String?,

    @SerializedName("logoSmall")
    val logoSmall: String?,

    @SerializedName("parentNodeId")
    val parentNodeId: String?,

    @SerializedName("productTags")
    val productTags: List<String>?,

    @SerializedName("sortOrder")
    val sortOrder: Int?,
)