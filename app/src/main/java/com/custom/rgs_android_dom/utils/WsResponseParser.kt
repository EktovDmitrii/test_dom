package com.custom.rgs_android_dom.utils

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.responses.ChatMessageResponse
import com.custom.rgs_android_dom.domain.chat.models.*
import com.google.gson.Gson
import org.json.JSONObject

class WsResponseParser(private val gson: Gson) {

    companion object {
        const val FIELD_EVENT = "event"
        const val FIELD_DATA = "data"
        const val FIELD_CALL_ID = "callId"
        const val FIELD_TOKEN = "token"
        const val FIELD_DECLINE_USER_ID = "declineUserId"
        const val FIELD_ROOM_ID = "roomId"

        const val EVENT_POSTED = "posted"
        const val EVENT_CHANNEL_VIEWED = "channel_viewed"
        const val EVENT_CALL_JOIN = "webrtc.call.can-join"
        const val EVENT_CALL_DECLINED = "webrtc.call.declined"
        const val EVENT_ROOM_CLOSED = "webrtc.room.closed"
        const val EVENT_ACCEPT_OFFER = "webrtc.call.accept-offer"
        const val EVENT_CALL_MISSED = "webrtc.call.missed"
    }

    fun parse(message: String, userId: String): WsMessageModel<*>?{
        try{
            val jsonResponse = JSONObject(message).getJSONObject(FIELD_DATA)
            return when (jsonResponse.getString(FIELD_EVENT)){
                EVENT_POSTED -> {
                    val chatMessageResponse = gson.fromJson(jsonResponse.getJSONObject(FIELD_DATA).toString().safeJSON(), ChatMessageResponse::class.java)
                    val chatMessageModel = ChatMapper.responseToChatMessage(chatMessageResponse, userId)

                    WsChatMessageModel(event = WsEvent.POSTED, message = chatMessageModel)
                }
                EVENT_CALL_JOIN -> {
                    val callId = jsonResponse.getString(FIELD_CALL_ID)
                    val token = jsonResponse.getJSONObject(FIELD_DATA).getString(FIELD_TOKEN)
                    val roomId = jsonResponse.getJSONObject(FIELD_DATA).getString(FIELD_ROOM_ID)

                    val callJoinInfoModel = CallJoinModel(callId = callId, token = token, roomId = roomId)
                    WsCallJoinModel(event = WsEvent.CALL_JOIN, callJoinInfo = callJoinInfoModel)
                }
                EVENT_CALL_DECLINED -> {
                    val declineUserId = jsonResponse.getJSONObject(FIELD_DATA).getString(FIELD_DECLINE_USER_ID)
                    WsCallDeclinedModel(event = WsEvent.CALL_DECLINED, declineUserId = declineUserId)
                }
                EVENT_ROOM_CLOSED -> {
                    WsRoomClosedModel(event = WsEvent.ROOM_CLOSED)
                }
                EVENT_ACCEPT_OFFER -> {
                    WsCallAcceptModel(event =  WsEvent.CALL_ACCEPT)
                }
                EVENT_CALL_MISSED -> {
                    WsCallMissedModel(event = WsEvent.CALL_MISSED)
                }
                else -> null
            }

        } catch (e: Exception){
            logException(this, e)
            return null
        }
    }
}