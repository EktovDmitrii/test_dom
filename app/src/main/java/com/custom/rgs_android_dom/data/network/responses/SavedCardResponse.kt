package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class SavedCardResponse(
    @SerializedName("bindingId")
    val bindingId: String?,

    @SerializedName("maskedPan")
    val maskedPan: String?,

    @SerializedName("expireDate")
    val expireDate: String?,

    @SerializedName("clientId")
    val clientId: String?,

    @SerializedName("paymentWay")
    val paymentWay: String?,

    @SerializedName("paymentSystem")
    val paymentSystem: String?,

    @SerializedName("bindingCategory")
    val bindingCategory: String?,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("isDefault")
    val isDefault: Boolean
)