package com.custom.rgs_android_dom.domain.chat.models

data class ChatDateDividerModel(
    val date: String,
    override val channelId: String
) : ChatItemModel(channelId)