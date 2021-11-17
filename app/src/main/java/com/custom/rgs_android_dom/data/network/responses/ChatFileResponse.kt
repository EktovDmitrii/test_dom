package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

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
    val miniPreview: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("size")
    val size: Long,

    @SerializedName("width")
    val width: Long
)