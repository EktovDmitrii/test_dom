package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class OrderAddress(
    val objectName: String? = null,
    val address: String? = null,
    val cityFiasId: String? = null,
    val cityName: String? = null,
    val fiasId: String? = null,
    val regionFiasId: String? = null,
    val regionName: String? = null
) : Serializable
