package com.custom.rgs_android_dom.domain.chat.models

enum class WsEvent {
    POSTED,
    CHANNEL_VIEWED,
    CALL_JOIN,
    CALL_DECLINED,
    ROOM_CLOSED,
    SOCKET_CONNECTED,
    SOCKET_DISCONNECTED,
    CALL_ACCEPT,
    CALL_MISSED
}