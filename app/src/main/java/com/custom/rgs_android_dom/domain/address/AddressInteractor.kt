package com.custom.rgs_android_dom.domain.address

import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.repositories.AddressRepository
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class AddressInteractor(private val addressRepository: AddressRepository){

    fun decodeLocation(newLocation: Point): Single<AddressItemModel>{
        return addressRepository.decodeLocation(newLocation)
    }

    fun onAddressSelected(address: AddressItemModel){
        addressRepository.selectAddress(address)
    }

    fun getAddressSelectedSubject(): PublishSubject<AddressItemModel>{
        return addressRepository.geSelectedAddressSubject()
    }

    fun getAddressSuggestions(query: String): Single<List<AddressItemModel>>{
        return addressRepository.getAddressSuggestions(query)
    }

}