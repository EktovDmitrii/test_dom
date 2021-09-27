package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel

object PropertyMapper {

    fun responseToProperty(response: PropertyItemResponse): PropertyItemModel {
        return PropertyItemModel(
            address = response.address,
            clientId = response.clientId,
            comment = response.comment ?: "",
            documents = response.documents?.map {
                PropertyDocument(link = it.link ?: "", name = it.name ?: "")
            } ?: listOf(),
            id = response.id,
            isOwn = response.isOwn?.equals("yes") == true,
            isRent = response.isRent?.equals("yes") == true,
            isTemporary = response.isTemporary?.equals("yes") == true,
            name = response.name,
            status = response.status ?: "",
            totalArea = response.totalArea,
            type = response.type
        )
    }
}