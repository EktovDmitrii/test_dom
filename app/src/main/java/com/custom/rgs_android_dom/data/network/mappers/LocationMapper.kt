package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.AddressItemResponse
import com.custom.rgs_android_dom.domain.location.models.AddressItemModel

object LocationMapper {

    fun responseToAddress(response: AddressItemResponse): AddressItemModel {
        return AddressItemModel(
            address = response.address,
            fiasId = response.fiasId,
            geocodeId = response.geocode
        )
    }

}