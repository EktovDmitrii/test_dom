package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class SaveTokenRequest(
    @SerializedName("token")
    val token: String
)