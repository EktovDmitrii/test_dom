package com.custom.rgs_android_dom.domain.chat.models

abstract class WsMessageModel<out T>(
    var event: WsEvent? = null,
    val data: T? = null
)