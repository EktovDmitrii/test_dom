package com.custom.rgs_android_dom.domain.chat

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class ChatInteractor(private val chatRepository: ChatRepository){

    val newMessageSubject = PublishSubject.create<ChatMessageModel>()

    fun getChatMessages(): Single<ArrayList<ChatMessageModel>> {
        return chatRepository.getChatMessages()
    }

    fun sendMessage(message: String){
        if (message.isNotEmpty()){
            val chatMessage = ChatMessageModel(
                message = message,
                sender = ChatMessageModel.Sender.ME
            )

            newMessageSubject.onNext(chatMessage)
        }

    }

}