package com.custom.rgs_android_dom.domain.location.models

import com.yandex.mapkit.geometry.Point

data class AddressItemModel(
    val address: String,
    val cityFiasId: String,
    val cityName: String,
    val coordinates: Point,
    val fiasId: String,
    val geocodeId: String,
    val regionFiasId: String,
    val regionName: String
)