package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable


data class DeliveryTime(
    val from: String? = null,
    val to: String? = null
) : Serializable
