package com.custom.rgs_android_dom.domain.chat

import com.custom.rgs_android_dom.domain.chat.models.ChatDateDividerModel
import com.custom.rgs_android_dom.domain.chat.models.ChatItemModel
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import com.custom.rgs_android_dom.domain.web_socket.models.WsChatMessageModel
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DAY_FULL_ONLY
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.getPeriod
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.LocalDateTime

class ChatInteractor(
    private val chatRepository: ChatRepository,
    private val webSocketRepository: WebSocketRepository
){
    companion object {
        private const val TYPE_SYSTEM_MESSAGE = "system_"
    }

    private var cachedChatItems = arrayListOf<ChatItemModel>()

    fun getChatItems(): Single<List<ChatItemModel>> {
        return chatRepository.getChatMessages().map { allMessages->
            val chatItems = arrayListOf<ChatItemModel>()

            val filteredMessages = allMessages.filter { !it.type.startsWith(TYPE_SYSTEM_MESSAGE) }
            if (filteredMessages.isNotEmpty()){
                filteredMessages.forEachIndexed { index, currentMessage ->
                    if (index == 0){
                        val dateDivider = ChatDateDividerModel(currentMessage.createdAt.formatTo(DATE_PATTERN_DAY_FULL_ONLY))
                        chatItems.add(dateDivider)
                    } else {
                        val prevMessage = if (index -1 >=0) filteredMessages[index-1] else null
                        val dateDivider = getDateDivider(currentMessage.createdAt, prevMessage?.createdAt)
                        if (dateDivider != null){
                            chatItems.add(dateDivider)
                        }
                    }
                    chatItems.add(currentMessage)
                }
            }
            cachedChatItems = chatItems
            return@map chatItems
        }
    }

    fun sendMessage(message: String): Completable {
        return chatRepository.sendMessage(message)
    }

    // TODO Improve this later, when we will have one interactor for chat
    fun getNewItemsSubject(): Observable<List<ChatItemModel>>{
        return webSocketRepository.getWsNewMessageSubject().map {eventModel->
            val chatItems = arrayListOf<ChatItemModel>()
            if (eventModel is WsChatMessageModel){
                eventModel.data?.takeIf {!it.type.startsWith(TYPE_SYSTEM_MESSAGE)}?.let{ currentMessage->
                    if (cachedChatItems.isNotEmpty()){
                        val prevMessage = cachedChatItems[cachedChatItems.size-1] as ChatMessageModel
                        getDateDivider(currentMessage.createdAt, prevMessage.createdAt)?.let { dateDivider->
                            chatItems.add(dateDivider)
                        }
                        chatItems.add(currentMessage)
                    } else {
                        val dateDivider = ChatDateDividerModel(currentMessage.createdAt.formatTo(DATE_PATTERN_DAY_FULL_ONLY))
                        chatItems.add(dateDivider)
                        chatItems.add(currentMessage)
                    }
                }
            }
            return@map chatItems
        }
    }

    private fun getDateDivider(currentMessageDate: LocalDateTime, previousMessageDate: LocalDateTime?): ChatDateDividerModel? {
        previousMessageDate?.let { previousMessageDate->
            if (previousMessageDate.getPeriod(currentMessageDate).days >0 ) {
                return ChatDateDividerModel(currentMessageDate.formatTo(DATE_PATTERN_DAY_FULL_ONLY))
            }
        }
        return null
    }
}