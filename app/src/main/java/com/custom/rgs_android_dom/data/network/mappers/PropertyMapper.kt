package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig.BASE_URL
import com.custom.rgs_android_dom.data.network.requests.PropertyAddressRequest
import com.custom.rgs_android_dom.data.network.requests.PropertyDocumentRequest
import com.custom.rgs_android_dom.data.network.requests.UpdatePropertyRequest
import com.custom.rgs_android_dom.data.network.responses.PostPropertyDocumentResponse
import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
import com.custom.rgs_android_dom.domain.property.models.PropertyAddressModel
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PostPropertyDocument
import java.io.File

object PropertyMapper {

    const val STORE_PATH = "$BASE_URL/api/store/"

    fun responseToProperty(response: PropertyItemResponse): PropertyItemModel {
        val isOwn = if (response.isOwn == "yes") true else if (response.isOwn == "no") false else null
        val isRent = if (response.isRent == "yes") true else if (response.isRent == "no") false else null
        val isTemporary = if (response.isTemporary == "yes") true else if (response.isTemporary == "no") false else null

        return PropertyItemModel(
            address = response.address?.let { address ->
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
            documents = (response.documents?.map {
                PropertyDocument(link = it.link ?: "", name = it.name ?: "")
            } ?: listOf()).toMutableList(),
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

    fun propertyToRequest(propertyItemModel: PropertyItemModel): UpdatePropertyRequest{
        val isOwn = if (propertyItemModel.isOwn == true) "yes" else if (propertyItemModel.isOwn == false) "no" else null
        val isRent = if (propertyItemModel.isRent == true) "yes" else if (propertyItemModel.isRent == false) "no" else null
        val isTemporary = if (propertyItemModel.isTemporary == true) "yes" else if (propertyItemModel.isTemporary == false) "no" else null
        val address = PropertyAddressRequest(
            address = if (propertyItemModel.address!=null) propertyItemModel.address.address else "",
            cityFiasId = if (propertyItemModel.address!=null) propertyItemModel.address.cityFiasId else "",
            cityName = if (propertyItemModel.address!=null) propertyItemModel.address.cityName else "",
            fiasId = if (propertyItemModel.address!=null) propertyItemModel.address.fiasId else "",
            regionFiasId =  if (propertyItemModel.address!=null)propertyItemModel.address.regionFiasId else "",
            regionName = if (propertyItemModel.address!=null) propertyItemModel.address.regionName else ""
        )
        return UpdatePropertyRequest(
            address = address,
            comment = propertyItemModel.comment,
            documents = propertyDocumentsToPropertyDocumentsRequest(propertyItemModel.documents),
            id = propertyItemModel.id,
            isOwn = isOwn,
            isRent = isRent,
            isTemporary = isTemporary,
            name = propertyItemModel.name,
            totalArea = propertyItemModel.totalArea

        )
    }

    fun responseToPostPropertyDocument(response: PostPropertyDocumentResponse): PostPropertyDocument {
        return PostPropertyDocument(fileId = response.id)
    }

    fun postPropertyDocumentToPropertyDocument(
        postPropertyDocument: PostPropertyDocument,
        file: File
    ): PropertyDocument {
        return PropertyDocument(
            link =  "$STORE_PATH${postPropertyDocument.fileId}",
            name = file.name
        )
    }

    fun propertyDocumentsToPropertyDocumentsRequest(documents: List<PropertyDocument>): List<PropertyDocumentRequest> {
        val result = mutableListOf<PropertyDocumentRequest>()
        documents.map { document ->
               result.add(PropertyDocumentRequest(
                   link = document.link,
                   name = document.name
               )
               )
        }
        return result
    }
}
