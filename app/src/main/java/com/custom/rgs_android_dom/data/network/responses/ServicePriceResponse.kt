package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ServicePriceResponse(

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("vatType")
    val vatType: String,

)