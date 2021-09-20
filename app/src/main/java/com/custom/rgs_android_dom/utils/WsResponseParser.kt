package com.custom.rgs_android_dom.utils

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.web_socket.models.WsMessageModel
import com.custom.rgs_android_dom.domain.web_socket.models.WsResponseModel
import com.google.gson.Gson
import org.json.JSONObject

class WsResponseParser(private val gson: Gson) {

    companion object {
        const val FILED_EVENT = "event"
        const val FIELD_DATA = "data"

        const val EVENT_POSTED = "posted"
        const val EVENT_CHANNEL_VIEWED = "channel_viewed"
    }

    fun parse(message: String, userId: String): WsResponseModel<*>?{
        try{
            val jsonResponse = JSONObject(message).getJSONObject(FIELD_DATA)
            return when (jsonResponse.getString(FILED_EVENT)){
                EVENT_POSTED -> {
                    val wsMessageModel = gson.fromJson(jsonResponse.toString(), WsMessageModel::class.java).apply {
                        data?.sender = if (data?.userId == userId) ChatMessageModel.Sender.ME else ChatMessageModel.Sender.OPPONENT
                    }
                    wsMessageModel
                }
                else -> null
            }

        } catch (e: Exception){
            logException(this, e)
            return null
        }
    }
}