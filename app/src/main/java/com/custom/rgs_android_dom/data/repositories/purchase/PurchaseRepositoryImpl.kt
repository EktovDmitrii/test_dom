package com.custom.rgs_android_dom.data.repositories.purchase

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.PurchaseMapper
import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.NewCardModel
import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import com.custom.rgs_android_dom.utils.safeLet
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class PurchaseRepositoryImpl(private val api: MSDApi) : PurchaseRepository {

    private val serviceOrdered = PublishSubject.create<String>()
    private val productPurchased = PublishSubject.create<String>()

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
        comment: String?,
        deliveryDate: String?,
        timeFrom: String?,
        timeTo: String?
    ): Single<String> {

        var orderRequest: OrderRequest? = null
        if (comment != null){
            orderRequest = OrderRequest(
                comment = comment
            )
        }

        safeLet(deliveryDate, timeFrom, timeTo){deliveryDate, timeFrom, timeTo->
            if (orderRequest == null){
                orderRequest = OrderRequest(
                    deliveryDate = deliveryDate,
                    deliveryTime = OrderTimeRequest(
                        timeFrom = timeFrom,
                        timeTo = timeTo
                    )
                )
            } else {
                orderRequest = orderRequest?.copy(
                    deliveryDate = deliveryDate,
                    deliveryTime = OrderTimeRequest(
                        timeFrom = timeFrom,
                        timeTo = timeTo
                    )
                )
            }
        }

        val purchaseRequest = PurchaseProductRequest(
            bindingId = bindingId,
            email = email,
            saveCard = saveCard,
            objectId = objectId,
            order = orderRequest
        )

        return api.makeProductPurchase(
            productId = productId,
            order = purchaseRequest
        ).map {
            PurchaseMapper.responseToPaymentUrl(it)
        }
    }

    override fun orderServiceOnBalance(
        serviceId: String,
        clientServiceId: String,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String,
        comment: String?
    ): Completable {
        val orderServiceRequest = OrderServiceRequest(
            clientServiceId = clientServiceId,
            comment = comment,
            deliveryDate = deliveryDate,
            deliveryTime = DeliveryTimeRequest(
                from = timeFrom,
                to = timeTo
            ),
            objectId = objectId
        )
        return api.orderServiceOnBalance(orderServiceRequest).doOnComplete {
            serviceOrdered.onNext(serviceId)
        }
    }

    override fun getServiceOrderedSubject(): PublishSubject<String> {
        return serviceOrdered
    }

    override fun getProductPurchasedSubject(): PublishSubject<String> {
        return productPurchased
    }

    override fun notifyProductPurchased(productId: String) {
        productPurchased.onNext(productId)
    }
}