package com.custom.rgs_android_dom.domain.chat.models

class WsCallDeclinedModel(event: WsEvent, declineUserId: String) : WsMessageModel<String>(event, declineUserId)