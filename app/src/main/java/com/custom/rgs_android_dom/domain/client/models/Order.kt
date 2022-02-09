package com.custom.rgs_android_dom.domain.client.models

import com.custom.rgs_android_dom.data.network.responses.DeliveryTimeResponse
import com.custom.rgs_android_dom.data.network.responses.ProviderResponse
import com.custom.rgs_android_dom.data.network.responses.ServicesResponse
import java.io.Serializable

data class Order(
    val id: String,
    val address: OrderAddress? = null,
    val client: OrderClient? = null,
    val closedAt: String? = null,
    val code: String? = null,
    val comment: String? = null,
    val createdAt: String? = null,
    val deliveryDate: String? = null,
    val deliveryTime: DeliveryTimeResponse? = null,
    val provider: ProviderResponse? = null,
    val refId: String? = null,
    val services: List<ServicesResponse>? = null,
    val status: String? = null
) : Serializable
