package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class EditClientRequest(
    @SerializedName("businessLine")
    val businessLine: String
)
