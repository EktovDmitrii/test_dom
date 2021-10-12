package com.custom.rgs_android_dom.domain.location

import com.custom.rgs_android_dom.data.network.mappers.LocationMapper
import com.custom.rgs_android_dom.domain.location.models.AddressItemModel
import com.custom.rgs_android_dom.domain.repositories.LocationRepository
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class LocationInteractor(private val locationRepository: LocationRepository){

    fun getCurrentLocation(): Single<Point> {
        return locationRepository.getCurrentLocation()
    }

    fun getDefaultLocation(): Point {
        return locationRepository.getDefaultLocation()
    }

    fun decodeLocation(newLocation: Point): Single<AddressItemModel>{
        return locationRepository.decodeLocation(newLocation)
    }

    fun onAddressSelected(address: AddressItemModel){
        locationRepository.selectAddress(address)
    }

    fun getAddressSelectedSubject(): PublishSubject<AddressItemModel>{
        return locationRepository.geSelectedAddressSubject()
    }

    fun getAddressSuggestions(query: String): Single<List<AddressItemModel>>{
        return locationRepository.getAddressSuggestions(query)
    }

}