package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(

    @SerializedName("fileIds")
    val fileIds: List<String>? = null,

    @SerializedName("message")
    val message: String? = null
)