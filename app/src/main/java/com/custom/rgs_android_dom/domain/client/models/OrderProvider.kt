package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class OrderProvider(
    val code: String? = null,
    val inn: String? = null,
    val name: String? = null,
    val providerId: String? = null
) : Serializable
