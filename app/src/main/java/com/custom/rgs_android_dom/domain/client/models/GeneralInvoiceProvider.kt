package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class GeneralInvoiceProvider (
    val agentType: Int? = null,
    val inn: String? = null,
    val name: String? = null,
    val phone: String? = null
) : Serializable
