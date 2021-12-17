package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductCoolOffResponse(

    @SerializedName("unitType")
    val unitType: String,

    @SerializedName("units")
    val units: Int

)