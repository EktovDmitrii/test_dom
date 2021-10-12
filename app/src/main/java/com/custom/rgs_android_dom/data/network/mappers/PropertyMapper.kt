package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
import com.custom.rgs_android_dom.domain.location.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyAddressModel
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.yandex.mapkit.geometry.Point

object PropertyMapper {

    fun responseToProperty(response: PropertyItemResponse): PropertyItemModel {
        val isOwn = if (response.isOwn == "yes") true else if (response.isOwn == "no") false else null
        val isRent = if (response.isRent == "yes") true else if (response.isRent == "no") false else null
        val isTemporary = if (response.isTemporary == "yes") true else if (response.isTemporary == "no") false else null

        return PropertyItemModel(
            address = response.address?.let { address->
                PropertyAddressModel(
                    address = address.address ?: "",
                    cityFiasId = address.cityFiasId ?: "",
                    cityName = address.cityName ?: "",
                    fiasId = address.fiasId ?: "",
                    regionFiasId = address.regionFiasId ?: "",
                    regionName = address.regionName ?: ""
                )
            },
            clientId = response.clientId,
            comment = response.comment ?: "",
            documents = response.documents?.map {
                PropertyDocument(link = it.link ?: "", name = it.name ?: "")
            } ?: listOf(),
            id = response.id,
            isOwn = isOwn,
            isRent = isRent,
            isTemporary = isTemporary,
            name = response.name,
            status = response.status ?: "",
            totalArea = response.totalArea,
            type = response.type
        )
    }
}