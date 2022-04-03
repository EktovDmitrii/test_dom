package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class OrderTimeResponse(

    @SerializedName("From")
    val from: String?,

    @SerializedName("To")
    val to: String?
)