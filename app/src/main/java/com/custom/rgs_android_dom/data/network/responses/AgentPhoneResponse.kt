package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class AgentPhoneResponse(

    @SerializedName("agentId")
    val agentId: String?,

    @SerializedName("phone")
    val phone: String

)