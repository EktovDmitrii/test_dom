package com.custom.rgs_android_dom.data.repositories.chat

data class InvoiceDetailsModel(
    val paymentUrl: String?,
    val orderId: String?,
    val paymentOrderId: String?,
    val bankOrderId: String?,
    val paymentEmail: String?,
    val orderCode: String?
)
