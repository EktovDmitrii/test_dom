package com.custom.rgs_android_dom.domain.client.models

data class CancelledTaskModel(
    val status: String,
    val subStatus: String,
    val taskId: String
)