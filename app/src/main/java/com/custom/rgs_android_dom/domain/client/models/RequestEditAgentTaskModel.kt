package com.custom.rgs_android_dom.domain.client.models

data class RequestEditAgentTaskModel(
    val status: RequestEditAgentStatus,
    val subStatus: String?,
    val taskId: String
)