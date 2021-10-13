package com.custom.rgs_android_dom.domain.location.models

import com.yandex.mapkit.geometry.Point
import java.io.Serializable

data class AddressItemModel(
    var addressString: String,
    val cityFiasId: String,
    val cityName: String,
    val coordinates: Point,
    val fiasId: String,
    val geocodeId: String,
    val regionFiasId: String,
    val regionName: String
): Serializable