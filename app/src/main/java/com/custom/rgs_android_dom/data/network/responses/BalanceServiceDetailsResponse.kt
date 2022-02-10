package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class BalanceServiceDetailsResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("serviceId")
    val serviceId: String,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("serviceName")
    val serviceName: String,

    @SerializedName("productIcon")
    val productIcon: String?,

    @SerializedName("serviceIcon")
    val serviceIcon: String?,

    @SerializedName("validityFrom")
    val validityFrom: DateTime?,

    @SerializedName("validityTo")
    val validityTo: DateTime?,

    @SerializedName("objectId")
    val objectId: String?,
)