package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class AddressItemResponse(
    @SerializedName("address")
    val address: String,

    @SerializedName("fiasId")
    val fiasId: String,

    @SerializedName("geocodeId")
    val geocode: String
)