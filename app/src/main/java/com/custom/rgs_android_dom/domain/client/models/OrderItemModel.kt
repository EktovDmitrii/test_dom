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
    val bills: List<Bill>
)

data class Bill(
    val id: Int,
    val type: BillType,
    val description: String
)

enum class BillType {
    MAIN, ADDITIONAL
}
