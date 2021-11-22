package com.custom.rgs_android_dom.utils

import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.responses.ChatMessageResponse
import com.custom.rgs_android_dom.domain.chat.models.CallJoinModel
import com.custom.rgs_android_dom.domain.chat.models.WsCallJoinModel
import com.custom.rgs_android_dom.domain.chat.models.WsChatMessageModel
import com.custom.rgs_android_dom.domain.chat.models.WsEventModel
import com.google.gson.Gson
import org.json.JSONObject

class WsResponseParser(private val gson: Gson) {

    companion object {
        const val FILED_EVENT = "event"
        const val FIELD_DATA = "data"
        const val FIELD_CALL_ID = "callId"
        const val FIELD_TOKEN = "token"

        const val EVENT_POSTED = "posted"
        const val EVENT_CHANNEL_VIEWED = "channel_viewed"
        const val EVENT_CALL_JOIN = "webrtc.call.can-join"
    }

    fun parse(message: String, userId: String): WsEventModel<*>?{
        try{
            val jsonResponse = JSONObject(message).getJSONObject(FIELD_DATA)
            return when (jsonResponse.getString(FILED_EVENT)){
                EVENT_POSTED -> {
                    val chatMessageResponse = gson.fromJson(jsonResponse.getJSONObject(FIELD_DATA).toString(), ChatMessageResponse::class.java)
                    val chatMessageModel = ChatMapper.responseToChatMessage(chatMessageResponse, userId)

                    WsChatMessageModel(event = WsEventModel.Event.POSTED, message = chatMessageModel)
                }
                EVENT_CALL_JOIN -> {
                    val callId = jsonResponse.getString(FIELD_CALL_ID)
                    val token = jsonResponse.getJSONObject(FIELD_DATA).getString(FIELD_TOKEN)

                    val callJoinInfoModel = CallJoinModel(callId = callId, token = token)
                    WsCallJoinModel(event = WsEventModel.Event.CALL_JOIN, callJoinInfo = callJoinInfoModel)
                }
                else -> null
            }

        } catch (e: Exception){
            logException(this, e)
            return null
        }
    }
}