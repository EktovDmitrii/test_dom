package com.custom.rgs_android_dom.domain.client.models

data class RequestEditClientTaskModel(
    val status: RequestEditClientStatus,
    val subStatus: String?,
    val taskId: String
)