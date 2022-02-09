package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ServiceDurationResponse(
    @SerializedName("unitType")
    val unitType: String,

    @SerializedName("units")
    val units: Int

)