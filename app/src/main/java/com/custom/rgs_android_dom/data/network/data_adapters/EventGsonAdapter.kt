package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.domain.chat.models.WsEvent
import com.custom.rgs_android_dom.utils.WsResponseParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class EventGsonAdapter : TypeAdapter<WsEvent>() {

    override fun write(out: JsonWriter, value: WsEvent?) {
        val eventString = when (value){
            WsEvent.POSTED -> {
                "posted"
            }
            WsEvent.CHANNEL_VIEWED -> {
                "channel_viewed"
            }
            WsEvent.CALL_JOIN -> {
                "webrtc.call.can-join"
            }
            WsEvent.CALL_DECLINED -> {
                "webrtc.call.declined"
            }
            else -> {
                ""
            }
        }
        out.value(eventString)
    }

    override fun read(jsonReader: JsonReader): WsEvent? {
        return when (jsonReader.nextString()) {
            WsResponseParser.EVENT_POSTED -> {
                WsEvent.POSTED
            }
            WsResponseParser.EVENT_CHANNEL_VIEWED -> {
                WsEvent.CHANNEL_VIEWED
            }
            WsResponseParser.EVENT_CALL_JOIN -> {
                WsEvent.CALL_JOIN
            }
            WsResponseParser.EVENT_CALL_DECLINED -> {
                WsEvent.CALL_DECLINED
            }
            else -> null
        }
    }
}
