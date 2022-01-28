package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.purchase_service.model.SavedCardModel
import io.reactivex.Single

interface PurchaseRepository {

    fun getSavedCards(): Single<List<SavedCardModel>>

    fun makeProductPurchase(
        productId: String,
        bindingId: String?,
        email: String,
        saveCard: Boolean,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String
    ): Single<String>
}