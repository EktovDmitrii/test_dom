package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.domain.web_socket.models.WsEventModel
import com.custom.rgs_android_dom.utils.WsResponseParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class EventGsonAdapter : TypeAdapter<WsEventModel.Event>() {

    override fun write(out: JsonWriter, value: WsEventModel.Event?) {
        val eventString = when (value){
            null -> {
                ""
            }
            WsEventModel.Event.POSTED -> {
                "posted"
            }
            WsEventModel.Event.CHANNEL_VIEWED -> {
                "channel_viewed"
            }
        }
        out.value(eventString)
    }

    override fun read(jsonReader: JsonReader): WsEventModel.Event? {
        return when (jsonReader.nextString()) {
            WsResponseParser.EVENT_POSTED -> {
                WsEventModel.Event.POSTED
            }
            WsResponseParser.EVENT_CHANNEL_VIEWED -> {
                WsEventModel.Event.CHANNEL_VIEWED
            }
            else -> null
        }
    }
}
