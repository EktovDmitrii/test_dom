package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class BalanceServiceDetailsResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("serviceId")
    val serviceId: String,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("serviceName")
    val serviceName: String,

    @SerializedName("productIcon")
    val productIcon: String?
)