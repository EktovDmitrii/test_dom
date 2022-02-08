package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class GeneralInvoicesResponse (
    @SerializedName("index")
    val index: Int,

    @SerializedName("invoices")
    val invoices: List<GeneralInvoice>,

    @SerializedName("total")
    val total: Int
)

data class GeneralInvoice (
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("amountTotal")
    val amountTotal: Int? = null,

    @SerializedName("cancelledAt")
    val cancelledAt: String? = null,

    @SerializedName("cardBindingId")
    val cardBindingId: String? = null,

    @SerializedName("client")
    val client: GeneralInvoiceClient? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("currency")
    val currency: Int? = null,

    @SerializedName("details")
    val details: GeneralInvoiceDetails? = null,

    @SerializedName("items")
    val items: List<GeneralInvoiceItem>,

    @SerializedName("maskedPan")
    val maskedPan : String? = null,

    @SerializedName("num")
    val num: String? = null,

    @SerializedName("paidAt")
    val paidAt: String? = null,

    @SerializedName("paymentSystem")
    val paymentSystem: String? = null,

    @SerializedName("paymentWay")
    val paymentWay: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("submittedAt")
    val submittedAt: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("vatTotal")
    val vatTotal: Int? = null
)

data class GeneralInvoiceItem (
    @SerializedName("amount")
    val amount: Int? = null,

    @SerializedName("itemCode")
    val itemCode: String? = null,

    @SerializedName("itemId")
    val itemId: String? = null,

    @SerializedName("measure")
    val measure: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("price")
    val price: Int? = null,

    @SerializedName("provider")
    val provider: GeneralInvoiceProvider? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("vatAmount")
    val vatAmount: Int? = null,

    @SerializedName("vatType")
    val vatType: Int? = null
)

data class GeneralInvoiceClient (
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("phone")
    val phone: String? = null
)

data class GeneralInvoiceDetails (
    @SerializedName("bankOrderId")
    val bankOrderId: String? = null,

    @SerializedName("orderCode")
    val orderCode: String? = null,

    @SerializedName("orderId")
    val orderId: String? = null,

    @SerializedName("paymentEmail")
    val paymentEmail: String? = null,

    @SerializedName("paymentOrderId")
    val paymentOrderId: String? = null,

    @SerializedName("paymentUrl")
    val paymentUrl: String? = null
)

data class GeneralInvoiceProvider (
    @SerializedName("agentType")
    val agentType: Int? = null,

    @SerializedName("inn")
    val inn: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("phone")
    val phone: String? = null
)
