package com.custom.rgs_android_dom.domain.client.models

data class OrderItemModel(
    val id: Int,
    val title: String,
    val status: String,
    val price: String,
    val date: String,
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
