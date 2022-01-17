package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ChatFilePreviewResponse(
    @SerializedName("contentSize")
    val contentSize: Long?,

    @SerializedName("contentType")
    val contentType: String?,

    @SerializedName("download")
    val download: Boolean?,

    @SerializedName("filename")
    val filename: String?
)