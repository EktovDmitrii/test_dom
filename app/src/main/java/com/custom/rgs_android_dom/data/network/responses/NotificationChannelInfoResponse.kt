package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class NotificationChannelInfoResponse(
    @SerializedName("type")
    val type: String,

    @SerializedName("enabled")
    val enabled: Boolean
)