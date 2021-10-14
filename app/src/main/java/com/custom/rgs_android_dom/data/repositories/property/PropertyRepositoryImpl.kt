package com.custom.rgs_android_dom.data.repositories.property

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.PropertyMapper
import com.custom.rgs_android_dom.data.network.requests.AddPropertyRequest
import com.custom.rgs_android_dom.data.network.requests.PropertyAddressRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class PropertyRepositoryImpl(private val api: MSDApi,
                             private val authSharedPreferences: AuthSharedPreferences) : PropertyRepository {

    private val propertyAddedSubject = PublishSubject.create<Unit>()

    override fun addProperty(
        name: String,
        type: String,
        address: AddressItemModel,
        isOwn: String?,
        isRent: String?,
        isTemporary: String?,
        totalArea: Float?,
        comment: String?
    ): Completable {
        val addPropertyRequest = AddPropertyRequest(
            name = name,
            type = type,
            address = PropertyAddressRequest(
                address = address.addressString,
                cityFiasId = address.cityFiasId,
                cityName = address.cityName,
                fiasId = address.fiasId,
                regionFiasId = address.regionFiasId,
                regionName = address.regionName
            ),
            isOwn = isOwn,
            isRent = isRent,
            isTemporary = isTemporary,
            totalArea = totalArea,
            comment = comment,
            documents = null
        )
        val clientId = authSharedPreferences.getClientId()
        return api.addProperty(clientId, addPropertyRequest).doOnComplete {
            propertyAddedSubject.onNext(Unit)
        }
    }

    override fun getAllProperty(): Single<List<PropertyItemModel>> {
        val clientId = authSharedPreferences.getClientId()
        return api.getAllProperty(clientId).map { response->
            response.objects?.map { PropertyMapper.responseToProperty(it) } ?: listOf()
        }
    }

    override fun getPropertyItem(objectId: String): Single<PropertyItemModel> {
        return api.getPropertyItem(objectId).map { response->
            PropertyMapper.responseToProperty(response)
        }
    }

    override fun getPropertyAddedSubject(): PublishSubject<Unit> {
        return propertyAddedSubject
    }

}