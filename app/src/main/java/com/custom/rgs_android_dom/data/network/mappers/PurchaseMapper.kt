package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PurchaseResponse
import com.custom.rgs_android_dom.data.network.responses.SavedCardResponse
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.CardType
import com.custom.rgs_android_dom.domain.purchase.models.SavedCardModel

object PurchaseMapper {

    fun responseToSavedCard(response: SavedCardResponse): CardModel {
        return SavedCardModel(
            id = response.bindingId ?: "",
            number = response.maskedPan ?: "",
            type = CardType.VISA,
            expireDate = response.expireDate ?: "",
            isDefault = response.isDefault
        )
    }

    fun responseToPaymentUrl(response: PurchaseResponse): String {
        return response.paymentUrl ?: ""
    }

}