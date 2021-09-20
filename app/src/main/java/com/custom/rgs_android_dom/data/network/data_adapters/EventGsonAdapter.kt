package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.domain.web_socket.models.WsResponseModel
import com.custom.rgs_android_dom.utils.WsResponseParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class EventGsonAdapter : TypeAdapter<WsResponseModel.Event>() {

    override fun write(out: JsonWriter, value: WsResponseModel.Event?) {
        val eventString = when (value){
            null -> {
                ""
            }
            WsResponseModel.Event.POSTED -> {
                "posted"
            }
            WsResponseModel.Event.CHANNEL_VIEWED -> {
                "channel_viewed"
            }
        }
        out.value(eventString)
    }

    override fun read(jsonReader: JsonReader): WsResponseModel.Event? {
        return when (jsonReader.nextString()) {
            WsResponseParser.EVENT_POSTED -> {
                WsResponseModel.Event.POSTED
            }
            WsResponseParser.EVENT_CHANNEL_VIEWED -> {
                WsResponseModel.Event.CHANNEL_VIEWED
            }
            else -> null
        }
    }
}
