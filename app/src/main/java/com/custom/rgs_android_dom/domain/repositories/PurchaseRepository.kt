package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseInfoModel
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
        timeTo: String?,
        withOrder: Boolean,
        clientPromoCodeId: String?,
        clientPrice: Int?
    ): Single<PurchaseInfoModel>

    fun orderServiceOnBalance(
        serviceId: String,
        clientServiceId: String,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String,
        comment: String?
    ): Single<Order>

    fun getServiceOrderedSubject(): PublishSubject<String>

    fun getProductPurchasedSubject(): PublishSubject<String>

    fun notifyProductPurchased(productId: String)

    fun deleteCard(bindingId: String): Completable

    fun getDeletedCardSubject(): PublishSubject<String>

    fun getActualProductPrice(productId: String, clientPromoCodeId: String?): Single<Int>
}