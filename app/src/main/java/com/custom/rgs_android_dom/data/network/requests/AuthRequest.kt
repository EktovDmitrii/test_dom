package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class AuthRequest(

    @SerializedName("AuthCode")
    val authCode: String? = null,

    @SerializedName("ChatLogin")
    val chatLogin: String? = null,

    @SerializedName("Password")
    val password: String? = null,

    @SerializedName("Username")
    val username: String? = null

)