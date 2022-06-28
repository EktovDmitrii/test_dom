package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.AddressItemResponse
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.yandex.mapkit.geometry.Point

object AddressMapper {

    fun responseToAddress(response: AddressItemResponse): AddressItemModel {
        return AddressItemModel(
            addressString = response.address,
            cityFiasId = response.cityFiasId,
            cityName = response.cityName,
            coordinates = response.coordinates?.let { coordinates ->
                Point(coordinates.lat, coordinates.long)
            },
            fiasId = response.fiasId,
            geocodeId = response.geocodeId,
            regionFiasId = response.regionFiasId,
            regionName = response.regionName
        )
    }

}