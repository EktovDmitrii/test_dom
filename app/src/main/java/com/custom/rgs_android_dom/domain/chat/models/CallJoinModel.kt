package com.custom.rgs_android_dom.domain.chat.models

data class CallJoinModel(
    val callId: String,
    val token: String,
    val roomId: String
)