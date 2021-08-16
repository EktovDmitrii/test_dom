package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("code")
    val code: String,

    @SerializedName("phone")
    val phone: String

)