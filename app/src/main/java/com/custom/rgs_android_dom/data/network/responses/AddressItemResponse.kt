package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class AddressItemResponse(
    @SerializedName("address")
    val address: String,

    @SerializedName("cityFiasId")
    val cityFiasId: String,

    @SerializedName("cityName")
    val cityName: String,

    @SerializedName("coordinates")
    val coordinates: CoordinatesResponse,

    @SerializedName("fiasId")
    val fiasId: String,

    @SerializedName("geocodeId")
    val geocodeId: String,

    @SerializedName("regionFiasId")
    val regionFiasId: String,

    @SerializedName("regionName")
    val regionName: String
)

class CoordinatesResponse(

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("long")
    val long: Double

)