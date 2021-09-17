package com.custom.rgs_android_dom.domain.chat

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import com.custom.rgs_android_dom.domain.web_socket.models.WsResponseModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class ChatInteractor(
    private val chatRepository: ChatRepository,
    private val webSocketRepository: WebSocketRepository
){

    fun getChatMessages(): Single<List<ChatMessageModel>> {
        return chatRepository.getChatMessages()
    }

    fun sendMessage(message: String): Completable {
        return chatRepository.sendMessage(message)
    }

    fun getWsNewMessageSubject(): PublishSubject<WsResponseModel<*>>{
        return webSocketRepository.getWsNewMessageSubject()
    }

}