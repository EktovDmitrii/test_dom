package com.custom.rgs_android_dom.domain.chat

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class ChatInteractor(private val chatRepository: ChatRepository){

    val newMessageSubject = PublishSubject.create<ChatMessageModel>()

    fun getChatMessages(): Single<List<ChatMessageModel>> {
        return chatRepository.getChatMessages()
    }

    fun sendMessage(message: String): Completable {
        return chatRepository.sendMessage(message)
            .doFinally{
                if (message.isNotEmpty()){
                    val chatMessage = ChatMessageModel(
                        channelId = "",
                        id = "",
                        userId = "",
                        files = listOf(),
                        message = message,
                        sender = ChatMessageModel.Sender.ME
                    )

                    newMessageSubject.onNext(chatMessage)
                }
            }
    }

}