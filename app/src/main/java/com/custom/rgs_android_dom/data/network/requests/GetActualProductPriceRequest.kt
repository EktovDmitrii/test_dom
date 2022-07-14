package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class GetActualProductPriceRequest(
    @SerializedName("businessLine")
    val businessLine: String,

    @SerializedName("clientPromoCodeId")
    val clientPromoCodeId: String?
)