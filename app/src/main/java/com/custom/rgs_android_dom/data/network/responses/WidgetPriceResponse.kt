package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class WidgetPriceResponse(

    @SerializedName("Amount")
    val amount: Int?,

    @SerializedName("VatType")
    val vatType: String?

)
