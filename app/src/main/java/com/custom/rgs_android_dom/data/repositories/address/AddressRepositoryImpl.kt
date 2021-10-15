package com.custom.rgs_android_dom.data.repositories.address

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.AddressMapper
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.repositories.AddressRepository
import com.yandex.mapkit.geometry.Point
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.lang.Exception

class AddressRepositoryImpl(private val api: MSDApi): AddressRepository {

    companion object {
        private const val COUNTRY = "Россия"
    }

    private val selectedAddressSubject = PublishSubject.create<AddressItemModel>()

    override fun decodeLocation(newLocation: Point): Single<AddressItemModel> {
        return api.getAddressByCoordinates(newLocation.latitude, newLocation.longitude).map {
            if (it.results != null){
                AddressMapper.responseToAddress(it.results[0])
            } else {
                throw Exception("Wrong decode result")
            }
        }
    }

    override fun selectAddress(addressModel: AddressItemModel) {
        selectedAddressSubject.onNext(addressModel)
    }

    override fun geSelectedAddressSubject(): PublishSubject<AddressItemModel> {
        return selectedAddressSubject
    }

    override fun getAddressSuggestions(query: String): Single<List<AddressItemModel>> {
        return api.getAddressSuggestions(query, COUNTRY).map {response->
            if (response.results != null){
                response.results.map {
                    AddressMapper.responseToAddress(it)
                }
            } else {
                listOf()
            }
        }
    }
}