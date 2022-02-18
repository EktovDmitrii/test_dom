package com.custom.rgs_android_dom.domain.chat.models

abstract class WsEventModel<out T>(
    var event: Event? = null,
    val data: T? = null
) {

    enum class Event {
        POSTED,
        CHANNEL_VIEWED,
        CALL_JOIN,
        CALL_DECLINED,
        ROOM_CLOSED,
        SOCKET_CONNECTED,
        SOCKET_DISCONNECTED
    }

}