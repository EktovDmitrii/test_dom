package com.custom.rgs_android_dom.domain.purchase

import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.models.*
import com.custom.rgs_android_dom.domain.purchase.view_states.ServiceOrderViewState
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_AND_TIME_FOR_PURCHASE
import com.custom.rgs_android_dom.utils.formatTo
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.*

class PurchaseInteractor(
    private val purchaseRepository: PurchaseRepository,
    private val catalogRepository: CatalogRepository
) {

    private var serviceOrderViewState = ServiceOrderViewState()

    val serviceOrderViewStateSubject = PublishSubject.create<ServiceOrderViewState>()
    private var isPropertyOptional = false

    fun setInitData(
        property: PropertyItemModel?,
        dateTime: PurchaseDateTimeModel?,
        deliveryType: DeliveryType?
    ) {
        setPropertyOptional(deliveryType)

        serviceOrderViewState = serviceOrderViewState.copy(
            property = property,
            orderDate = dateTime
        )
        validateServiceOrderViewState()
    }

    fun getSavedCards(): Single<List<CardModel>> {
        return purchaseRepository.getSavedCards()
    }

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
        withOrder: Boolean
    ): Single<PurchaseInfoModel> {
        return purchaseRepository.makeProductPurchase(
            productId = productId,
            bindingId = bindingId,
            email = email,
            saveCard = saveCard,
            objectId = objectId,
            comment = comment,
            deliveryDate = deliveryDate,
            timeFrom = timeFrom,
            timeTo = timeTo,
            withOrder = withOrder
        )
    }

    fun selectServiceOrderProperty(property: PropertyItemModel){
        serviceOrderViewState = serviceOrderViewState.copy(property = property)
        validateServiceOrderViewState()
    }

    fun selectServiceOrderComment(comment: String){
        serviceOrderViewState = serviceOrderViewState.copy(comment = comment.ifEmpty { null })
        validateServiceOrderViewState()
    }

    fun selectServiceOrderDate(orderDate: PurchaseDateTimeModel){
        serviceOrderViewState = serviceOrderViewState.copy(orderDate = orderDate)
        validateServiceOrderViewState()
    }

    fun orderServiceOnBalance(productId: String, serviceId: String): Single<Order> {
        return catalogRepository.getAvailableServiceInProduct(productId, serviceId)
            .flatMap {
                purchaseRepository.orderServiceOnBalance(
                    serviceId = serviceId,
                    clientServiceId = it.id,
                    objectId = serviceOrderViewState.property?.id ?: "",
                    deliveryDate = serviceOrderViewState.orderDate?.selectedDate?.formatTo(
                        DATE_PATTERN_DATE_AND_TIME_FOR_PURCHASE
                    ) + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).removePrefix("GMT"),
                    timeFrom = serviceOrderViewState.orderDate?.selectedPeriodModel?.timeFrom ?: "",
                    timeTo = serviceOrderViewState.orderDate?.selectedPeriodModel?.timeTo ?: "",
                    comment = serviceOrderViewState.comment
                )
            }
    }

    fun getServiceOrderedSubject(): PublishSubject<String>{
        return purchaseRepository.getServiceOrderedSubject()
    }

    fun getProductPurchasedSubject(): PublishSubject<String>{
        return purchaseRepository.getProductPurchasedSubject()
    }

    fun notifyProductPurchased(productId: String){
        purchaseRepository.notifyProductPurchased(productId)
    }

    private fun setPropertyOptional(deliveryType: DeliveryType?) {
        isPropertyOptional = deliveryType == DeliveryType.ONLINE
    }

    private fun validateServiceOrderViewState(){
        val isServiceOrderTextViewEnabled = (serviceOrderViewState.orderDate != null &&
                ((!isPropertyOptional && serviceOrderViewState.property != null) || isPropertyOptional)
                )
        serviceOrderViewState = serviceOrderViewState.copy(isServiceOrderTextViewEnabled = isServiceOrderTextViewEnabled)
        serviceOrderViewStateSubject.onNext(serviceOrderViewState)
    }

}
