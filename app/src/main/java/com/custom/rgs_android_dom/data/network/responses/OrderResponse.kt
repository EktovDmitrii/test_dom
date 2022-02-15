package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class OrdersResponse(
    @SerializedName("index")
    val index: Int,

    @SerializedName("orders")
    val orders: List<OrderResponse>?,

    @SerializedName("total")
    val total: Int
)

data class OrderResponse (
    @SerializedName("id")
    val id: String,

    @SerializedName("address")
    val address: OrderAddressResponse? = null,

    @SerializedName("object")
    val orderObject: OrderObjectResponse? = null,

    @SerializedName("client")
    val client: OrderClientResponse? = null,

    @SerializedName("closedAt")
    val closedAt: String? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("deliveryDate")
    val deliveryDate: String? = null,

    @SerializedName("deliveryTime")
    val deliveryTime: DeliveryTimeResponse? = null,

    @SerializedName("provider")
    val provider: ProviderResponse? = null,

    @SerializedName("refId")
    val refId: String? = null,

    @SerializedName("services")
    val services: List<ServicesResponse>? = null,

    @SerializedName("status")
    val status: String? = null
)

data class ServicesResponse (
    @SerializedName("clientProductId")
    val clientProductId: String? = null,

    @SerializedName("clientServiceId")
    val clientServiceId: String? = null,

    @SerializedName("defaultProduct")
    val defaultProduct: Boolean? = null,

    @SerializedName("productIcon")
    val productIcon: String? = null,

    @SerializedName("productName")
    val productName: String? = null,

    @SerializedName("productPrice")
    val productPrice: Int,

    @SerializedName("serviceCode")
    val serviceCode: String? = null,

    @SerializedName("serviceDeliveryType")
    val serviceDeliveryType: String? = null,

    @SerializedName("serviceFixPrice")
    val serviceFixPrice: Boolean? = null,

    @SerializedName("serviceIdInGroup")
    val serviceIdInGroup: String? = null,

    @SerializedName("serviceLogoMiddle")
    val serviceLogoMiddle: String? = null,

    @SerializedName("serviceName")
    val serviceName: String? = null,

    @SerializedName("serviceType")
    val serviceType: String? = null,

    @SerializedName("serviceVersionId")
    val serviceVersionId: String? = null
)

data class ProviderResponse (
    @SerializedName("code")
    val code: String? = null,

    @SerializedName("inn")
    val inn: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("providerId")
    val providerId: String? = null
)

data class OrderClientResponse (
    @SerializedName("clientId")
    val clientId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("phone")
    val phone: String? = null
)

data class OrderAddressResponse (
    @SerializedName("address")
    val address: String? = null,

    @SerializedName("cityFiasId")
    val cityFiasId: String? = null,

    @SerializedName("cityName")
    val cityName: String? = null,

    @SerializedName("fiasId")
    val fiasId: String? = null,

    @SerializedName("regionFiasId")
    val regionFiasId: String? = null,

    @SerializedName("regionName")
    val regionName: String? = null
)

data class OrderObjectResponse(
    @SerializedName("objectId")
    val objectId: String? = null,

    @SerializedName("objectName")
    val objectName: String? = null
)

data class DeliveryTimeResponse (
    @SerializedName("from")
    val from: String? = null,

    @SerializedName("to")
    val to: String? = null
)
