package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductShortResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("code")
    val code: String,

    @SerializedName("versionId")
    val versionId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("tags")
    val tags: List<String>?,

    @SerializedName("defaultProduct")
    val defaultProduct: Boolean?,

    @SerializedName("iconLink")
    val iconLink: String?
)