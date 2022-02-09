package com.custom.rgs_android_dom.domain.client.models

import org.joda.time.LocalDate

data class OrderItemModel(
    val id: String,
    val productId: String,
    val defaultProduct: Boolean,
    val deliveryTime: String,
    val fix: Boolean,
    val title: String,
    val status: OrderStatus,
    val price: Int?,
    val date: LocalDate,
    val icon: String,
    val description: String,
    val invoices: List<InvoiceItemModel>
)

data class InvoiceItemModel(
    val orderId: String,
    val amount: Int? = null,
    val vatType: Int? = null,
    val type: InvoiceType,
    val description: String
)

enum class InvoiceType {
    MAIN, ADDITIONAL
}
