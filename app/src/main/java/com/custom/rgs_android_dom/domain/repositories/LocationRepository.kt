package com.custom.rgs_android_dom.domain.repositories

import com.yandex.mapkit.geometry.Point
import io.reactivex.Single

interface LocationRepository {

    fun getLocation(): Single<Point>

    fun getDefaultLocation(): Point

}