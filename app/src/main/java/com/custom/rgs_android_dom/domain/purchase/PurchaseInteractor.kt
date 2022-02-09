package com.custom.rgs_android_dom.domain.purchase

import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.models.*
import com.custom.rgs_android_dom.domain.purchase.view_states.ServiceOrderViewState
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import io.reactivex.Single

class PurchaseInteractor(
    private val purchaseRepository: PurchaseRepository,
    private val catalogRepository: CatalogRepository
) {

    fun getSavedCards(): Single<List<CardModel>> {
        return purchaseRepository.getSavedCards()
    }

    fun makeProductPurchase(
        productId: String,
        bindingId: String?,
        email: String,
        saveCard: Boolean,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String,
    ): Single<String> {
        return purchaseRepository.makeProductPurchase(
            productId = productId,
            bindingId = bindingId,
            email = email,
            saveCard = saveCard,
            objectId = objectId,
            deliveryDate = deliveryDate,
            timeFrom = timeFrom,
            timeTo = timeTo
        )
    }
}
