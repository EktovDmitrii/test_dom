package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.LocalDateTime

data class CallInfoModel(
    val acceptedAt: LocalDateTime?,
    val channelId: String,
    val declinedAt: LocalDateTime?,
    val id: String,
    val initiatorUserId: String,
    val recipientUserId: String,
    val registeredAt: LocalDateTime?,
    val taskId: String
)