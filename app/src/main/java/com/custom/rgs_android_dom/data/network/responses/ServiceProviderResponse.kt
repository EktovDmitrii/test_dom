package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ServiceProviderResponse(
    @SerializedName("name")
    val name: String?,

    @SerializedName("providerId")
    val providerId: String?,

    @SerializedName("type")
    val type: String?
)