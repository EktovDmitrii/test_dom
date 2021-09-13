package com.custom.rgs_android_dom.data.repositories.chat

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import io.reactivex.Single

class ChatRepositoryImpl : ChatRepository {

    override fun getChatMessages(): Single<ArrayList<ChatMessageModel>> {
        return Single.fromCallable {
            val messages = arrayListOf<ChatMessageModel>()
            messages.add(
                ChatMessageModel(
                    message = "Добрый день!",
                    sender= ChatMessageModel.Sender.OPPONENT
                )
            )
            messages.add(
                ChatMessageModel(
                    message = "Опишите, пожалуйста, причину ДТП. Можно видео или аудиозвонком — будет быстрее.",
                    sender = ChatMessageModel.Sender.OPPONENT
                )
            )
            return@fromCallable messages
        }
    }
}