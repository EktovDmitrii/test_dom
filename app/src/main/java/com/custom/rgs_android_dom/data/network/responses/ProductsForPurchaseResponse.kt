package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductsForPurchaseResponse(

    @SerializedName("total")
    val total: Int,

    @SerializedName("products")
    val products: List<ProductShortResponse>?

)