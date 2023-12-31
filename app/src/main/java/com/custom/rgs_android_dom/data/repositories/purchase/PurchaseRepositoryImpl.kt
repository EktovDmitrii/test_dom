package com.custom.rgs_android_dom.data.repositories.purchase

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.OrdersMapper
import com.custom.rgs_android_dom.data.network.mappers.PurchaseMapper
import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.NewCardModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseInfoModel
import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import com.custom.rgs_android_dom.utils.safeLet
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class PurchaseRepositoryImpl(private val api: MSDApi) : PurchaseRepository {

    private val serviceOrdered = PublishSubject.create<String>()
    private val productPurchased = PublishSubject.create<String>()
    private val deletedCardSubject = PublishSubject.create<String>()

    override fun getSavedCards(): Single<List<CardModel>> {
        return api.getSavedCards(BuildConfig.MERCHANT_TYPE).toSingle().map { list ->
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
        timeTo: String?,
        withOrder: Boolean,
        clientPromoCodeId: String?,
        clientPrice: Int?
    ): Single<PurchaseInfoModel> {

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
                    ),
                    withOrder = true
                )
            } else {
                orderRequest = orderRequest?.copy(
                    deliveryDate = deliveryDate,
                    deliveryTime = OrderTimeRequest(
                        timeFrom = timeFrom,
                        timeTo = timeTo
                    ),
                    withOrder = true
                )
            }
        }

        if (orderRequest != null) {
            orderRequest = orderRequest!!.copy(
                withOrder = withOrder
            )
        }

        val purchaseRequest = PurchaseProductRequest(
            bindingId = bindingId,
            email = email,
            saveCard = saveCard,
            objectId = objectId,
            order = orderRequest,
            businessLine = BuildConfig.BUSINESS_LINE,
            clientPromoCodeId = clientPromoCodeId,
            clientPrice = clientPrice
        )
        return api.makeProductPurchase(
            productId = productId,
            order = purchaseRequest
        ).map {
            PurchaseMapper.responseToPurchaseInfo(it)
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
    ): Single<Order> {
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
        return api.orderServiceOnBalance(orderServiceRequest, true).map {response->
            serviceOrdered.onNext(serviceId)
            return@map OrdersMapper.responseToOrder(orderResponse = response)
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

    override fun deleteCard(bindingId: String): Completable {
        return api.deleteCard(bindingId, BuildConfig.MERCHANT_TYPE).doOnComplete {
            deletedCardSubject.onNext(bindingId)
        }
    }

    override fun getDeletedCardSubject(): PublishSubject<String> {
        return deletedCardSubject
    }

    override fun getActualProductPrice(productId: String, clientPromoCodeId: String?): Single<Int> {
        val getActualProductPriceRequest = GetActualProductPriceRequest(
            businessLine = BuildConfig.BUSINESS_LINE,
            clientPromoCodeId = clientPromoCodeId
        )
        return api.getActualProductPrice(productId, getActualProductPriceRequest).map {
            it.price
        }
    }

}