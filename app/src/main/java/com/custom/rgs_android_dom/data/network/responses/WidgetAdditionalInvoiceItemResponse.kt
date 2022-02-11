package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class WidgetAdditionalInvoiceItemResponse(

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("quantity")
    val quantity: Int
)