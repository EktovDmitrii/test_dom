package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import io.reactivex.Completable
import io.reactivex.Single

interface ChatRepository {

    fun getChatMessages(): Single<List<ChatMessageModel>>

    fun sendMessage(message: String): Completable

}