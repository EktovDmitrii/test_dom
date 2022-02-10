package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class GeneralInvoice(
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
) : Serializable {

    fun getFullPrice(): Int {
        var fullPrice = 0
        items.forEach {
            fullPrice += ((it.price ?: 0) * (it.quantity ?: 0))
        }
        return fullPrice
    }
}
