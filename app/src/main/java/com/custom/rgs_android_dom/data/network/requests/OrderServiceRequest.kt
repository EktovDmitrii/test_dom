package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class OrderServiceRequest(
    @SerializedName("clientServiceId")
    val clientServiceId: String,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("deliveryDate")
    val deliveryDate: String,

    @SerializedName("deliveryTime")
    val deliveryTime: DeliveryTimeRequest,

    @SerializedName("objectId")
    val objectId: String
)

data class DeliveryTimeRequest(
    @SerializedName("from")
    val from: String,

    @SerializedName("to")
    val to: String
)