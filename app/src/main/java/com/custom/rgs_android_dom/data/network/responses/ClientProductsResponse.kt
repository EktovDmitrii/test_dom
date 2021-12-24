package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ClientProductsResponse(

    @SerializedName("clientProducts")
    val clientProducts: List<ClientProductResponse>?,

    @SerializedName("index")
    val index: Int,

    @SerializedName("total")
    val total: Int
)