package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ServiceItemsResponse(
    @SerializedName("index")
    val index: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("items")
    val items: List<ServiceResponse>?
)