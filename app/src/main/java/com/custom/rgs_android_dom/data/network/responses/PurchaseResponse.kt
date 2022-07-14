package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class PurchaseResponse(
    @SerializedName("clientProductId")
    val clientProductId: String?,

    @SerializedName("paymentUrl")
    val paymentUrl: String?,

    @SerializedName("invoiceId")
    val invoiceId: String?,

    @SerializedName("paymentOrderId")
    val paymentOrderId: String?,

    @SerializedName("bankOrderId")
    val bankOrderId: String?,

    @SerializedName("orderId")
    val orderId: String?,

    @SerializedName("noPaymentRequired")
    val noPaymentRequired: Boolean?,
)