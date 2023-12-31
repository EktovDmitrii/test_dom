package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig.BASE_URL
import com.custom.rgs_android_dom.data.network.requests.PropertyAddressRequest
import com.custom.rgs_android_dom.data.network.requests.PropertyDocumentRequest
import com.custom.rgs_android_dom.data.network.requests.UpdatePropertyRequest
import com.custom.rgs_android_dom.data.network.responses.PostPropertyDocumentResponse
import com.custom.rgs_android_dom.data.network.responses.PropertyItemResponse
import com.custom.rgs_android_dom.data.network.responses.TaskModificationsResponse
import com.custom.rgs_android_dom.domain.property.models.*
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
                    regionName = address.regionName ?: "",
                    entrance = address.entrance,
                    floor = address.floor
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
            type = response.type,
            photoLink = response.photoLink?.let { "$STORE_PATH$it" }
        )
    }

    fun propertyToRequest(propertyItemModel: PropertyItemModel): UpdatePropertyRequest{
        val isOwn = if (propertyItemModel.isOwn == true) "yes" else if (propertyItemModel.isOwn == false) "no" else "unspecified"
        val isRent = if (propertyItemModel.isRent == true) "yes" else if (propertyItemModel.isRent == false) "no" else "unspecified"
        val isTemporary = if (propertyItemModel.isTemporary == true) "yes" else if (propertyItemModel.isTemporary == false) "no" else "unspecified"
        val address = PropertyAddressRequest(
            address = if (propertyItemModel.address!=null) propertyItemModel.address.address else "",
            cityFiasId = propertyItemModel.address?.cityFiasId?.ifEmpty { null },
            cityName = propertyItemModel.address?.cityName?.ifEmpty { null },
            fiasId = propertyItemModel.address?.fiasId?.ifEmpty { null },
            regionFiasId =  propertyItemModel.address?.regionFiasId?.ifEmpty { null },
            regionName = propertyItemModel.address?.regionName?.ifEmpty { null },
            entrance = propertyItemModel.address?.entrance,
            floor = propertyItemModel.address?.floor,
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
            totalArea = propertyItemModel.totalArea,
            photoLink = propertyItemModel.photoLink?.removePrefix(STORE_PATH)
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

    fun responseToModifications(response: TaskModificationsResponse): List<ModificationTask> {
        return response.tasks?.map {
            ModificationTask(
                taskId = it.taskId,
                status = it.status,
                subStatus = it.subStatus,
            )
        } ?: emptyList()
    }
}
