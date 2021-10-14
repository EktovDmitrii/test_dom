package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class CoordinatesResponse(

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("long")
    val long: Double
)