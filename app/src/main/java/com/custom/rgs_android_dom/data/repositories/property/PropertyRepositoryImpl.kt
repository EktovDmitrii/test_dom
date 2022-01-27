package com.custom.rgs_android_dom.data.repositories.property

import android.net.Uri
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.PropertyMapper
import com.custom.rgs_android_dom.data.network.requests.AddPropertyRequest
import com.custom.rgs_android_dom.data.network.requests.PropertyAddressRequest
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PostPropertyDocument
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import com.custom.rgs_android_dom.utils.toMultipartFormData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.io.File

class PropertyRepositoryImpl(
    private val api: MSDApi,
    private val clientSharedPreferences: ClientSharedPreferences
) : PropertyRepository {

    companion object {
        const val STORE_BUCKET = "docs"
        const val STORE_EXTENSION = ""
        const val STORE_METADATA = ""
    }

    private val propertyAddedSubject = PublishSubject.create<Unit>()
    private val propertyDocumentsUploadedSubject = PublishSubject.create<List<Uri>>()
    private val propertyDocumentsDeletedSubject = PublishSubject.create<PropertyItemModel>()


    override fun addProperty(
        name: String,
        type: String,
        address: AddressItemModel,
        isOwn: String?,
        isRent: String?,
        isTemporary: String?,
        totalArea: Float?,
        comment: String?,
        documents: List<PropertyDocument>
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
            documents = PropertyMapper.propertyDocumentsToPropertyDocumentsRequest(documents)
        )
        return api.addProperty(addPropertyRequest).doOnComplete {
            propertyAddedSubject.onNext(Unit)
        }
    }

    override fun getAllProperty(): Single<List<PropertyItemModel>> {
        return api.getAllProperty().map { response ->
            response.objects?.map { PropertyMapper.responseToProperty(it) } ?: listOf()
        }
    }

    override fun getPropertyItem(objectId: String): Single<PropertyItemModel> {
        return api.getPropertyItem(objectId).map { response ->
            PropertyMapper.responseToProperty(response)
        }
    }

    override fun getPropertyAddedSubject(): PublishSubject<Unit> {
        return propertyAddedSubject
    }

    override fun getPropertyDocumentUploadedSubject(): PublishSubject<List<Uri>> {
        return propertyDocumentsUploadedSubject
    }

    override fun getPropertyDocumentDeletedSubject(): PublishSubject<PropertyItemModel> {
        return propertyDocumentsDeletedSubject
    }

    override fun onFileToDeleteSelected(propertyItemModel: PropertyItemModel) {
        propertyDocumentsDeletedSubject.onNext(propertyItemModel)
    }

    override fun onFilesToUploadSelected(files: List<Uri>) {
        propertyDocumentsUploadedSubject.onNext(files)
    }

    override fun postPropertyDocument(file: File): Single<PostPropertyDocument> {
        return api.postPropertyDocument(
            file.toMultipartFormData(),
            STORE_BUCKET,
            STORE_EXTENSION,
            STORE_METADATA
        )
            .map { PropertyMapper.responseToPostPropertyDocument(it) }
    }

    override fun updateProperty(
        objectId: String,
        propertyItemModel: PropertyItemModel
    ): Single<PropertyItemModel> {
        val updatePropertyRequest = PropertyMapper.propertyToRequest(propertyItemModel)
        return api.updatePropertyItem(objectId, updatePropertyRequest).map { response ->
            PropertyMapper.responseToProperty(response)
        }
    }
}
