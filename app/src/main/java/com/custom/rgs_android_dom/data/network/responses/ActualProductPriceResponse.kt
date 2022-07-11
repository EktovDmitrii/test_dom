package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ActualProductPriceResponse(
    @SerializedName("comment")
    val comment: String?,

    @SerializedName("price")
    val price: Int
)