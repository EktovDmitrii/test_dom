package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateAgentRequest(

    @SerializedName("code")
    val code: String,

    @SerializedName("phone")
    val phone: String

)