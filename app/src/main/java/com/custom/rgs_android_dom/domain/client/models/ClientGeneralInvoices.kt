package com.custom.rgs_android_dom.domain.client.models

data class GeneralInvoice (
    val id: String? = null,
    val amountTotal: Int? = null,
    val cancelledAt: String? = null,
    val cardBindingId: String? = null,
    val client: GeneralInvoiceClient? = null,
    val createdAt: String? = null,
    val currency: Int? = null,
    val details: GeneralInvoiceDetails? = null,
    val items: List<GeneralInvoiceItem>,
    val maskedPan : String? = null,
    val num: String? = null,
    val paidAt: String? = null,
    val paymentSystem: String? = null,
    val paymentWay: String? = null,
    val status: String? = null,
    val submittedAt: String? = null,
    val type: String? = null,
    val vatTotal: Int? = null
)

data class GeneralInvoiceItem (
    val amount: Int? = null,
    val itemCode: String? = null,
    val itemId: String? = null,
    val measure: String? = null,
    val name: String? = null,
    val price: Int? = null,
    val provider: GeneralInvoiceProvider? = null,
    val quantity: Int? = null,
    val vatAmount: Int? = null,
    val vatType: Int? = null
)

data class GeneralInvoiceClient (
    val email: String? = null,
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null
)

data class GeneralInvoiceDetails (
    val bankOrderId: String? = null,
    val orderCode: String? = null,
    val orderId: String? = null,
    val paymentEmail: String? = null,
    val paymentOrderId: String? = null,
    val paymentUrl: String? = null
)

data class GeneralInvoiceProvider (
    val agentType: Int? = null,
    val inn: String? = null,
    val name: String? = null,
    val phone: String? = null
)
