package com.custom.rgs_android_dom.domain.chat.view_states

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel

data class ChatViewState(
    val newMessage: ChatMessageModel? = null
)