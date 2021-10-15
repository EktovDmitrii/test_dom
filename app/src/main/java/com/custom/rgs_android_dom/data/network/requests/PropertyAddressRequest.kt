package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class PropertyAddressRequest(

    @SerializedName("address")
    val address: String,

    @SerializedName("cityFiasId")
    val cityFiasId: String,

    @SerializedName("cityName")
    val cityName: String,

    @SerializedName("fiasId")
    val fiasId: String,

    @SerializedName("regionFiasId")
    val regionFiasId: String,

    @SerializedName("regionName")
    val regionName: String

)