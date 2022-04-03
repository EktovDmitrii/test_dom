package com.custom.rgs_android_dom.domain.chat.models

data class CallConnectionModel(
    val channelId: String,
    val id: String,
    val initiatorUserId: String,
    val recipientUserId: String,
    val taskId: String
)