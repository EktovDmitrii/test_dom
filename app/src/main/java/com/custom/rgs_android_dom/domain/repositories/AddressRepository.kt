package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface AddressRepository {

    fun geSelectedAddressSubject(): PublishSubject<AddressItemModel>

    fun decodeLocation(newLocation: Point): Single<AddressItemModel>

    fun selectAddress(addressModel: AddressItemModel)

    fun getAddressSuggestions(query: String): Single<List<AddressItemModel>>

}