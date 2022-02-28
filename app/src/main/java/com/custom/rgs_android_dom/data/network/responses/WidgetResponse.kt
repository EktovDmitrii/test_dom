package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime


open class WidgetResponse(

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
    val serviceName: String?,

    // specific to widget type OrderDefaultProduct

    @SerializedName("deliveryTime")
    val deliveryTime: String?,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("objAddr")
    val objAddr: String?,

    @SerializedName("objId")
    val objId: String?,

    @SerializedName("objName")
    val objName: String?,

    @SerializedName("objPhotoLink")
    val objPhotoLink: String?,

    @SerializedName("objType")
    val objType: String?,

    @SerializedName("orderDate")
    val orderDate: DateTime?,

    @SerializedName("orderTime")
    val orderTime: OrderTimeResponse?,

    @SerializedName("price")
    val price1: Int?,

    // specific to widget type OrderComplexProduct

    @SerializedName("clientServiceId")
    val clientServiceId: String?

)