package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ServiceShortResponse(
    @SerializedName("businessLine")
    val businessLine: String?,

    @SerializedName("priceAmount")
    val priceAmount: Long?,

    @SerializedName("providerId")
    val providerId: String?,

    @SerializedName("providerName")
    val providerName: String?,

    @SerializedName("quantity")
    val quantity: Long?,

    @SerializedName("serviceCode")
    val serviceCode: String?,

    @SerializedName("serviceId")
    val serviceId: String,

    @SerializedName("serviceName")
    val serviceName: String?,

    @SerializedName("serviceDeliveryType")
    val serviceDeliveryType: String,

    @SerializedName("serviceVersionId")
    val serviceVersionId: String?
)