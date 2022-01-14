package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductServicesResponse(
    @SerializedName("index")
    val index: Long?,

    @SerializedName("items")
    val items: List<ServiceShortResponse>?,

    @SerializedName("total")
    val total: Long?
)