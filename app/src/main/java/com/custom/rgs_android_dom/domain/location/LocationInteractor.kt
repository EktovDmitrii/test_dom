package com.custom.rgs_android_dom.domain.location

import com.custom.rgs_android_dom.domain.repositories.LocationRepository
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single

class LocationInteractor(private val locationRepository: LocationRepository){

    fun getLocation(): Single<Point> {
        return locationRepository.getLocation()
    }

    fun getDefaultLocation(): Point {
        return locationRepository.getDefaultLocation()
    }

}