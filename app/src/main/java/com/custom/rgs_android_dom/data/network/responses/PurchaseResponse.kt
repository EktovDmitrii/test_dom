package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class PurchaseResponse(
    @SerializedName("clientProductId")
    val email: String?,

    @SerializedName("paymentUrl")
    val paymentUrl: String?,

    @SerializedName("invoiceId")
    val invoiceId: String?,

    @SerializedName("paymentOrderId")
    val paymentOrderId: String?,

    @SerializedName("bankOrderId")
    val bankOrderId: String?,
)