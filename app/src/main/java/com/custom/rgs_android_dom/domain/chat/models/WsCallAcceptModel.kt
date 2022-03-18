package com.custom.rgs_android_dom.domain.chat.models

class WsCallAcceptModel(
    event: WsEvent,
    val callerId: String,
    val callId: String,
    val caller: Sender
) : WsMessageModel<String>(event)