package com.custom.rgs_android_dom.domain.chat.models

class WsCallJoinModel(event: WsEvent, callJoinInfo: CallJoinModel) : WsMessageModel<CallJoinModel>(event, callJoinInfo)