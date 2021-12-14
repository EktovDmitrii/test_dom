package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class InsuranceProductResponse(
    @SerializedName("productId")
    val productId: String,

    @SerializedName("programId")
    val programId: String
)