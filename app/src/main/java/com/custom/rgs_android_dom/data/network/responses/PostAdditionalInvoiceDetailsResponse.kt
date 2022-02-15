package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class PostAdditionalInvoiceDetailsResponse(

    @SerializedName("paymentUrl")
    val paymentUrl: String?,

    @SerializedName("orderId")
    val orderId: String?,

    @SerializedName("paymentOrderId")
    val paymentOrderId: String?,

    @SerializedName("bankOrderId")
    val bankOrderId: String?,

    @SerializedName("paymentEmail")
    val paymentEmail: String?,

    @SerializedName("orderCode")
    val orderCode: String?
)
