package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

data class ChatMessageResponse(
    @SerializedName("channelId")
    val channelId: String,

    @SerializedName("files")
    val files: List<ChatFileResponse>?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("message")
    val message: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("createdAt")
    val createdAt: DateTime,

    @SerializedName("type")
    val type: String,

    @SerializedName("details")
    val details: WidgetResponse?
)