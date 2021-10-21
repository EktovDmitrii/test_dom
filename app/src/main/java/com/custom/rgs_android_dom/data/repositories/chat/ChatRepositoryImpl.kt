package com.custom.rgs_android_dom.data.repositories.chat

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.requests.SendMessageRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class ChatRepositoryImpl(private val api: MSDApi,
                         private val clientSharedPreferences: ClientSharedPreferences
) : ChatRepository {

    override fun getChatMessages(): Single<List<ChatMessageModel>> {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""

        return api.getChatMessages(channelId, 1000, 0).map {
            ChatMapper.responseToChatMessages(it, client?.userId ?: "")
        }.map {
            it.reversed()
        }

    }

    override fun sendMessage(message: String): Completable {
        val request = SendMessageRequest(message = message)

        val client = clientSharedPreferences.getClient()

        val channelId = client?.getChatChannelId() ?: ""
        val userId = client?.userId ?: ""

        return api.postMessage(userId, channelId, request)
    }
}