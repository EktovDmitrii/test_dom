package com.custom.rgs_android_dom.domain.chat.models

class WsCallDeclinedModel(event: Event, declineUserId: String) : WsEventModel<String>(event, declineUserId)