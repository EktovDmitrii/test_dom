package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class GeneralInvoiceDetails(
    val bankOrderId: String? = null,
    val orderCode: String? = null,
    val orderId: String? = null,
    val paymentEmail: String? = null,
    val paymentOrderId: String? = null,
    val paymentUrl: String? = null
) : Serializable
