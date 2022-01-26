package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class BalanceServicesResponse(
    @SerializedName("total")
    val total: Int,

    @SerializedName("index")
    val index: Int,

    @SerializedName("services")
    val services: List<BalanceServiceDetailsResponse>?,

    @SerializedName("balance")
    val balance: List<BalanceServiceResponse>?
)