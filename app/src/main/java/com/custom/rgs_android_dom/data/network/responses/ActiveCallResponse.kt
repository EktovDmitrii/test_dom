package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ActiveCallResponse(
    @SerializedName("acceptedAt")
    val acceptedAt: DateTime?,

    @SerializedName("channelId")
    val channelId: String,

    @SerializedName("registeredAt")
    val registeredAt: DateTime?
)