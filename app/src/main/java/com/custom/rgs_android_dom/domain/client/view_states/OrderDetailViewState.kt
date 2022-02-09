package com.custom.rgs_android_dom.domain.client.view_states

import com.custom.rgs_android_dom.domain.client.models.OrderStatus


data class OrderDetailViewState(
    val id: String,
    val orderStatus: OrderStatus,
    val paymentStatus: String,
    val serviceName: String,
    val address: String,
    val dateTime: String,
    val comment: String,
    val paymentDetail: String? = null
) {
    fun getOrderStateTitle(): String = "Заказ ${orderStatus.value.toLowerCase()}"
}
