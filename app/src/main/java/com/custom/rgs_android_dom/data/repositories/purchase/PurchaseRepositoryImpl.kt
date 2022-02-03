package com.custom.rgs_android_dom.data.repositories.purchase

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.PurchaseMapper
import com.custom.rgs_android_dom.data.network.requests.OrderRequest
import com.custom.rgs_android_dom.data.network.requests.OrderTimeRequest
import com.custom.rgs_android_dom.data.network.requests.PurchaseProductRequest
import com.custom.rgs_android_dom.domain.purchase.model.CardModel
import com.custom.rgs_android_dom.domain.purchase.model.NewCardModel
import com.custom.rgs_android_dom.domain.purchase.model.SavedCardModel
import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import io.reactivex.Single

class PurchaseRepositoryImpl(private val api: MSDApi) : PurchaseRepository {

    override fun getSavedCards(): Single<List<CardModel>> {
        return api.getSavedCards().toSingle().map { list ->
            list.map { PurchaseMapper.responseToSavedCard(it) }.plus(NewCardModel())
        }.onErrorResumeNext {
            Single.just(listOf(NewCardModel()))
        }
    }

    override fun makeProductPurchase(
        productId: String,
        bindingId: String?,
        email: String,
        saveCard: Boolean,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String
    ): Single<String> {
        val orderRequest = PurchaseProductRequest(
            bindingId = bindingId,
            email = email,
            saveCard = saveCard,
            objectId = objectId,
            order = OrderRequest(
                deliveryDate = deliveryDate,
                deliveryTime = OrderTimeRequest(
                    timeFrom = timeFrom,
                    timeTo = timeTo
                )
            )
        )
        return api.makeProductPurchase(
            productId = productId,
            order = orderRequest
        ).map {
            PurchaseMapper.responseToPaymentUrl(it)
        }
    }
}