package com.custom.rgs_android_dom.domain.client.models

import com.custom.rgs_android_dom.data.network.responses.OrderStatus
import org.joda.time.LocalDate

data class OrderItemModel(
    val id: String,
    val title: String,
    val status: OrderStatus,
    val price: Int?,
    val date: LocalDate,
    val icon: String,
    val description: String,
    val invoices: List<InvoiceItemModel>
)

data class InvoiceItemModel(
    val id: Int,
    val orderId: String,
    val type: InvoiceType,
    val description: String
)

enum class InvoiceType {
    MAIN, ADDITIONAL
}
