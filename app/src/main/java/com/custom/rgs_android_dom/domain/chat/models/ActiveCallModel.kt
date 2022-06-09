package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.DateTime

data class ActiveCallModel(
    val acceptedAt: DateTime?,
    val channelId: String,
    val registeredAt: DateTime?
)