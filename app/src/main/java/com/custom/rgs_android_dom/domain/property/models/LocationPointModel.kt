package com.custom.rgs_android_dom.domain.property.models

import com.yandex.mapkit.geometry.Point

data class LocationPointModel(
    val point: Point,
    val zoom: Float
)