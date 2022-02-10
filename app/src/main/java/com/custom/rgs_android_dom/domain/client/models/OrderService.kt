package com.custom.rgs_android_dom.domain.client.models

import java.io.Serializable

data class OrderService(
    val clientProductId: String? = null,
    val clientServiceId: String? = null,
    val defaultProduct: Boolean? = null,
    val productIcon: String? = null,
    val productName: String? = null,
    val productPrice: Int,
    val serviceCode: String? = null,
    val serviceDeliveryType: String? = null,
    val serviceFixPrice: Boolean? = null,
    val serviceIdInGroup: String? = null,
    val serviceLogoMiddle: String? = null,
    val serviceName: String? = null,
    val serviceType: String? = null,
    val serviceVersionId: String? = null
) : Serializable
