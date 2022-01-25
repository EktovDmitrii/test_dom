package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PurchaseResponse
import com.custom.rgs_android_dom.data.network.responses.SavedCardResponse
import com.custom.rgs_android_dom.domain.purchase_service.model.SavedCardModel

object PurchaseMapper {

    fun responseToSavedCard(response: SavedCardResponse): SavedCardModel {
        return SavedCardModel(
            id = response.bindingId ?: "",
            number = response.maskedPan ?: "",
            type = SavedCardModel.CardType.VISA,
            expireDate = response.expireDate ?: "",
            isDefault = response.isDefault
        )
    }

    fun responseToPaymentUrl(response: PurchaseResponse): String {
        return response.paymentUrl ?: ""
    }

}