package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.location.models.AddressItemModel
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface LocationRepository {

    fun geSelectedAddressSubject(): PublishSubject<AddressItemModel>

    fun getCurrentLocation(): Single<Point>

    fun getDefaultLocation(): Point

    fun decodeLocation(newLocation: Point): Single<AddressItemModel>

    fun selectAddress(addressModel: AddressItemModel)

    fun getAddressSuggestions(query: String): Single<List<AddressItemModel>>

}