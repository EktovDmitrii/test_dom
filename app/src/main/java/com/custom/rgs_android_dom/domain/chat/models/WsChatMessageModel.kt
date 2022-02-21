package com.custom.rgs_android_dom.domain.chat.models

class WsChatMessageModel(event: WsEvent, message: ChatMessageModel) : WsMessageModel<ChatMessageModel>(event, message)