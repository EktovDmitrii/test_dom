package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductPriceResponse(

    @SerializedName("amount")
    val amount: Int?,

    @SerializedName("vatType")
    val vatType: String?,

    @SerializedName("fix")
    val fix: Boolean

)