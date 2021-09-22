package com.custom.rgs_android_dom.data.repositories.property

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.PropertyMapper
import com.custom.rgs_android_dom.data.network.requests.AddPropertyRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import io.reactivex.Completable
import io.reactivex.Single

class PropertyRepositoryImpl(private val api: MSDApi,
                             private val authSharedPreferences: AuthSharedPreferences) : PropertyRepository {

    override fun addProperty(
        name: String,
        type: String,
        address: String,
        isOwn: String?,
        isRent: String?,
        isTemporary: String?,
        totalArea: Float?,
        comment: String?
    ): Completable {
        val addPropertyRequest = AddPropertyRequest(
            name = name,
            type = type,
            address = address,
            isOwn = isOwn,
            isRent = isRent,
            isTemporary = isTemporary,
            totalArea = totalArea,
            comment = comment,
            documents = null
        )
        val clientId = authSharedPreferences.getClientId()
        return api.addProperty(clientId, addPropertyRequest)
    }

    override fun getAllProperty(): Single<List<PropertyItemModel>> {
        val clientId = authSharedPreferences.getClientId()
        return api.getAllProperty(clientId).map { response->
            response.objects?.map { PropertyMapper.responseToProperty(it) } ?: listOf()
        }
    }

}