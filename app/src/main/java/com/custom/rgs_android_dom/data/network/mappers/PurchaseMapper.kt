package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PurchaseResponse
import com.custom.rgs_android_dom.data.network.responses.SavedCardResponse
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.CardType
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseInfoModel
import com.custom.rgs_android_dom.domain.purchase.models.SavedCardModel
import com.google.gson.annotations.SerializedName

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

    fun responseToPurchaseInfo(response: PurchaseResponse): PurchaseInfoModel {
        return PurchaseInfoModel(
            clientProductId = response.clientProductId ?: "",
            paymentUrl = response.paymentUrl ?: "",
            invoiceId = response.invoiceId ?: "",
            paymentOrderId = response.paymentOrderId ?: "",
            bankOrderId = response.bankOrderId ?: "",
            orderId = response.orderId ?: "",
            noPaymentRequired = response.noPaymentRequired ?: false
        )
    }

}