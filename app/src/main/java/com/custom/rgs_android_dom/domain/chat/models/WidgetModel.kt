package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.DateTime

sealed class WidgetModel(open val widgetType: WidgetType) {

    data class WidgetOrderProductModel(
        val avatar: String?,
        val description: String?,
        val name: String?,
        val price: WidgetPriceModel?,
        val productId: String?,
        override val widgetType: WidgetType
    ) : WidgetModel(widgetType)

    data class WidgetOrderDefaultProductModel(
        val deliveryTime: String?,
        val icon: String?,
        val name: String?,
        val objAddr: String?,
        val objId: String?,
        val objName: String?,
        val objPhotoLink: String?,
        val objType: String?,
        val orderDate: DateTime?,
        val orderTime: OrderTimeModel?,
        val productId: String?,
        override val widgetType: WidgetType,
        val price: Int?
    ) : WidgetModel(widgetType)

    data class WidgetAdditionalInvoiceModel(
        val amount: Int?,
        val invoiceId: String?,
        val items: List<WidgetAdditionalInvoiceItemModel>? = listOf(),
        val orderId: String?,
        val paymentUrl: String?,
        val serviceLogo: String?,
        val serviceName: String?,
        override val widgetType: WidgetType
    ) : WidgetModel(widgetType)

    data class WidgetOrderComplexProductModel(
        val clientServiceId: String?,
        val deliveryTime: String?,
        val icon: String?,
        val name: String?,
        val objAddr: String?,
        val objId: String?,
        val objName: String?,
        val objPhotoLink: String?,
        val objType: String?,
        val orderDate: DateTime?,
        val orderTime: OrderTimeModel?,
        override val widgetType: WidgetType
    ) : WidgetModel(widgetType)

}

enum class WidgetType {
    GeneralInvoicePayment,
    OrderComplexProduct,
    OrderDefaultProduct,
    Product
}