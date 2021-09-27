package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.property.info.ProperyInfoItem
import io.reactivex.Completable
import io.reactivex.Single

interface PropertyRepository {

    fun addProperty(
        name: String,
        type: String,
        address: String,
        isOwn: String?,
        isRent: String?,
        isTemporary: String?,
        totalArea: Float?,
        comment: String?,
    ): Completable

    fun getAllProperty(): Single<List<PropertyItemModel>>

    fun getPropertyItem(objectId: String): Single<PropertyItemModel>

}