package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class AgentHolderResponse(

    @SerializedName("assignPhone")
    val assignPhone: String?,

    @SerializedName("phones")
    val phones: List<AgentPhoneResponse>,

    @SerializedName("agent")
    val agent: AgentResponse

)