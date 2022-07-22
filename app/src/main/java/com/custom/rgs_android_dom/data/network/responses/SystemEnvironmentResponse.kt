package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class SystemEnvironmentResponse(
    @SerializedName("key")
    val key: String?,

    @SerializedName("value")
    val value: String?
)