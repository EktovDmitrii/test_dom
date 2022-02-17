package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class WidgetResponse(

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("price")
    val price: WidgetPriceResponse?,

    @SerializedName("productId")
    val productId: String?,

    @SerializedName("widgetType")
    val widgetType: String,

    @SerializedName("amount")
    val amount: Int?,

    @SerializedName("invoiceId")
    val invoiceId: String?,

    @SerializedName("items")
    val items: List<WidgetAdditionalInvoiceItemResponse>?,

    @SerializedName("orderId")
    val orderId: String?,

    @SerializedName("paymentUrl")
    val paymentUrl: String?,

    @SerializedName("serviceLogo")
    val serviceLogo: String?,

    @SerializedName("serviceName")
    val serviceName: String?

)