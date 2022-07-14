package com.custom.rgs_android_dom.domain.purchase.models

data class PurchaseInfoModel(
    val clientProductId: String,
    val paymentUrl: String,
    val invoiceId: String,
    val paymentOrderId: String,
    val bankOrderId: String,
    val orderId: String,
    val noPaymentRequired: Boolean
)