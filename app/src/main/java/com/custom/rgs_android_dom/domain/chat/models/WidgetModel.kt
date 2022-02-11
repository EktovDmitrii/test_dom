package com.custom.rgs_android_dom.domain.chat.models

data class WidgetModel(
    val avatar: String,
    val description: String,
    val name: String,
    val price: WidgetPriceModel,
    val productId: String,
    val widgetType: String
)
