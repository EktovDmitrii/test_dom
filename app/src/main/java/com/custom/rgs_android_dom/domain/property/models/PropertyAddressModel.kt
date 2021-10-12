package com.custom.rgs_android_dom.domain.property.models

data class PropertyAddressModel(
    val address: String,
    val cityFiasId: String,
    val cityName: String,
    val fiasId: String,
    val regionFiasId: String,
    val regionName: String
)