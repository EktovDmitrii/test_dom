package com.custom.rgs_android_dom.domain.web_socket.models

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel

class WsChatMessageModel(event: Event, message: ChatMessageModel) : WsEventModel<ChatMessageModel>(event, message)