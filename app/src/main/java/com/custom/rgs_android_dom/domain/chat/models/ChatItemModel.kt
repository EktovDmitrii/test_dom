package com.custom.rgs_android_dom.domain.chat.models

abstract class ChatItemModel constructor(
    open val channelId: String = "",
    open val id: String? = null
)