package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class BalanceServiceResponse(
    @SerializedName("serviceId")
    val serviceId: String,

    @SerializedName("clientServiceId")
    val clientServiceId: String,

    @SerializedName("available")
    val available: Int,

    @SerializedName("total")
    val total: Int
)