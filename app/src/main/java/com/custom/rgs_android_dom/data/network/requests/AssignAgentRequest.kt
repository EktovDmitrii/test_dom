package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class AssignAgentRequest(
    @SerializedName("agentCode")
    val agentCode: String,

    @SerializedName("agentPhone")
    val agentPhone: String,

    @SerializedName("assignType")
    val assignType: String
)