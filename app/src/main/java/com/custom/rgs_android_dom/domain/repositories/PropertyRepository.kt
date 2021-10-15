package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

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
    ): Completable

    fun getAllProperty(): Single<List<PropertyItemModel>>

    fun getPropertyItem(objectId: String): Single<PropertyItemModel>

    fun getPropertyAddedSubject(): PublishSubject<Unit>

}