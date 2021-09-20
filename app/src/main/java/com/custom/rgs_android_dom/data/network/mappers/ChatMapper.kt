package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.ChatMessageResponse
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel

object ChatMapper{

    fun responseToChatMessages(response: List<ChatMessageResponse>, userId: String): List<ChatMessageModel> {
        return response.map { messageResponse->
            ChatMessageModel(
                channelId = messageResponse.channelId,
                files = messageResponse.files?.map { fileResponse->
                    ChatFileModel(
                        extension = fileResponse.extension,
                        hasPreviewImage= fileResponse.hasPreviewImage,
                        height = fileResponse.height,
                        id = fileResponse.id,
                        mimeType = fileResponse.mimeType,
                        miniPreview = fileResponse.miniPreview,
                        name = fileResponse.name,
                        size = fileResponse.size,
                        width = fileResponse.width
                    )
                },
                id = messageResponse.id,
                message = messageResponse.message,
                userId = messageResponse.userId,
                sender = if (messageResponse.userId == userId) ChatMessageModel.Sender.ME else ChatMessageModel.Sender.OPPONENT
            )
        }
    }
}