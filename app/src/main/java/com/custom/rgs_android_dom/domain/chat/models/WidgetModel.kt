package com.custom.rgs_android_dom.domain.chat.models

data class WidgetModel(
    val avatar: String,
    val description: String,
    val name: String,
    val price: WidgetPriceModel,
    val productId: String,
    val widgetType: String,
    val amount: Int?,
    val invoiceId: String?,
    val items: List<WidgetAdditionalInvoiceItemModel>? = listOf(),
    val orderId: String?,
    val paymentUrl: String?,
    val serviceLogo: String?,
    val serviceName: String?
)
