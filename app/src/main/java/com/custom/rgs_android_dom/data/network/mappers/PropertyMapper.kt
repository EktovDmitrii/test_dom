package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel

object PropertyMapper {

    fun responseToProperty(response: PropertyItemResponse): PropertyItemModel {
        val isOwn = if (response.isOwn == "yes") true else if (response.isOwn == "no") false else null
        val isRent = if (response.isRent == "yes") true else if (response.isRent == "no") false else null
        val isTemporary = if (response.isTemporary == "yes") true else if (response.isTemporary == "no") false else null

        return PropertyItemModel(
            address = response.address,
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