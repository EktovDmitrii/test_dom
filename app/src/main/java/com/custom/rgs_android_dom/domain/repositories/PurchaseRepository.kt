package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

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

    fun orderServiceOnBalance(
        serviceId: String,
        clientServiceId: String,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String,
        comment: String?
    ): Completable

    fun getServiceOrderedSubject(): PublishSubject<String>

    fun getProductPurchasedSubject(): PublishSubject<String>
}