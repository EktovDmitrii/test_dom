package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.purchase.model.CardModel
import io.reactivex.Single

interface PurchaseRepository {

    fun getSavedCards(): Single<List<CardModel>>

    fun makeProductPurchase(
        productId: String,
        bindingId: String?,
        email: String,
        saveCard: Boolean,
        objectId: String,
        comment: String?,
        deliveryDate: String?,
        timeFrom: String?,
        timeTo: String?
    ): Single<String>
}