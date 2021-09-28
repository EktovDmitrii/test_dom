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
    val type: String
)

data class ChatFileResponse(
    @SerializedName("extension")
    val extension: String,

    @SerializedName("hasPreviewImage")
    val hasPreviewImage: Boolean,

    @SerializedName("height")
    val height: Int,

    @SerializedName("id")
    val id: String,

    @SerializedName("mimeType")
    val mimeType: String,

    @SerializedName("miniPreview")
    val miniPreview: List<Int>,

    @SerializedName("name")
    val name: String,

    @SerializedName("size")
    val size: Long,

    @SerializedName("width")
    val width: Long
)