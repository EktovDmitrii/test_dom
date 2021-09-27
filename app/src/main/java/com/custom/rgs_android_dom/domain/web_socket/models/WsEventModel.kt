package com.custom.rgs_android_dom.domain.web_socket.models

abstract class WsEventModel<out T>(
    var event: Event? = null,
    val data: T? = null
) {

    enum class Event {
        POSTED,
        CHANNEL_VIEWED
    }

}