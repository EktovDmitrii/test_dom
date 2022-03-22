package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class RequestEditAgentTaskResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("subStatus")
    val subStatus: String?,

    @SerializedName("taskId")
    val taskId: String
)