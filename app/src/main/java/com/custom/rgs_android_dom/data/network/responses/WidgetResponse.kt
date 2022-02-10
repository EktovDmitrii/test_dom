package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class WidgetResponse(

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: WidgetPriceResponse,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("widgetType")
    val widgetType: String
)
