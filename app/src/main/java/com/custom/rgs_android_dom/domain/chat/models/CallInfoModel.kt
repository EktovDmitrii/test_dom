package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.LocalDateTime

data class CallInfoModel(
    val channelId: String,
    val id: String,
    val initiatorUserId: String,
    val recipientUserId: String,
    val taskId: String
)