package com.custom.rgs_android_dom.domain.repositories

import android.net.Uri
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.ModificationTask
import com.custom.rgs_android_dom.domain.property.models.PostPropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File

interface PropertyRepository {

    fun addProperty(
        name: String,
        type: String,
        address: AddressItemModel,
        isOwn: String?,
        isRent: String?,
        isTemporary: String?,
        totalArea: Float?,
        comment: String?,
        documents: List<PropertyDocument>,
        floor: Int?,
        entrance: Int?
    ): Completable

    fun updatePropertyInfo(
        objectId: String,
        name: String,
        type: String,
        address: AddressItemModel,
        isOwn: String?,
        isRent: String?,
        isTemporary: String?,
        photoLink: String?,
        totalArea: Float?,
        comment: String?,
        floor: Int?,
        entrance: Int?,
        documents: List<PropertyDocument>
    ): Completable

    fun getAllProperty(): Single<List<PropertyItemModel>>

    fun getPropertyItem(objectId: String): Single<PropertyItemModel>

    fun getPropertyAddedSubject(): PublishSubject<Unit>

    fun getPropertyDocumentUploadedSubject(): PublishSubject<List<Uri>>

    fun getPropertyDocumentDeletedSubject(): PublishSubject<PropertyItemModel>

    fun getPropertyAvatarChangedSubject(): PublishSubject<String>

    fun getPropertyAvatarRemovedSubject(): PublishSubject<Unit>

    fun onFileToDeleteSelected(documentList: PropertyItemModel)

    fun onFilesToUploadSelected(files: List<Uri>)

    fun postPropertyDocument(file: File): Single<PostPropertyDocument>

    fun updateProperty(
        objectId: String,
        propertyItemModel: PropertyItemModel
    ): Single<PropertyItemModel>

    fun getEditPropertyRequestedSubject(): BehaviorSubject<Boolean>

    fun requestEditProperty(objectId: String): Completable

    fun getModifications(objectId: String): Single<List<ModificationTask>>

    fun deleteProperty(objectId: String): Completable

    fun getPropertyDeletedSubject(): PublishSubject<String>
}
