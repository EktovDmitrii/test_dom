package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class OrderClient(
    val clientId: String? = null,
    val name: String? = null,
    val phone: String? = null
) : Serializable
