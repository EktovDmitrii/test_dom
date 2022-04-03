package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class RequestEditAgentTasksResponse(
    @SerializedName("tasks")
    val tasks: List<RequestEditAgentTaskResponse>?
)