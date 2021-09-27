package com.custom.rgs_android_dom.utils

import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.responses.ChatMessageResponse
import com.custom.rgs_android_dom.domain.web_socket.models.WsChatMessageModel
import com.custom.rgs_android_dom.domain.web_socket.models.WsEventModel
import com.google.gson.Gson
import org.json.JSONObject

class WsResponseParser(private val gson: Gson) {

    companion object {
        const val FILED_EVENT = "event"
        const val FIELD_DATA = "data"

        const val EVENT_POSTED = "posted"
        const val EVENT_CHANNEL_VIEWED = "channel_viewed"
    }

    fun parse(message: String, userId: String): WsEventModel<*>?{
        try{
            val jsonResponse = JSONObject(message).getJSONObject(FIELD_DATA)
            return when (jsonResponse.getString(FILED_EVENT)){
                EVENT_POSTED -> {
                    val chatMessageResponse = gson.fromJson(jsonResponse.getJSONObject(FIELD_DATA).toString(), ChatMessageResponse::class.java)
                    val chatMessageModel = ChatMapper.responseToChatMessage(chatMessageResponse, userId)

                    val wsChatMessageModel = WsChatMessageModel(event = WsEventModel.Event.POSTED, message = chatMessageModel)
                    wsChatMessageModel
                }
                else -> null
            }

        } catch (e: Exception){
            logException(this, e)
            return null
        }
    }
}