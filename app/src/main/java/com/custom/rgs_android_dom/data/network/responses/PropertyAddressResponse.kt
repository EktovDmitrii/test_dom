package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class PropertyAddressResponse(
    @SerializedName("address")
    val address: String?,

    @SerializedName("city_fias_id")
    val cityFiasId: String?,

    @SerializedName("city_name")
    val cityName: String?,

    @SerializedName("fias_id")
    val fiasId: String?,

    @SerializedName("region_fias_id")
    val regionFiasId: String?,

    @SerializedName("region_name")
    val regionName: String?,

    @SerializedName("entrance")
    val entrance: Int? = null,

    @SerializedName("floor")
    val floor: Int? = null
)