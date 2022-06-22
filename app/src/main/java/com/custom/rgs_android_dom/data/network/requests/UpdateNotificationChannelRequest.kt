package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateNotificationChannelRequest(
    @SerializedName("enabled")
    val enabled: Boolean,

    @SerializedName("type")
    val type: String
)