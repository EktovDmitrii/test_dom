package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.Duration

data class CallInfoModel(
    val state: CallState = CallState.IDLE,
    val consultant: ChannelMemberModel? = null,
    val duration: Duration? = null,
    val error: String? = null,
    val channelId: String? = null,
    val callType: CallType? = null
)