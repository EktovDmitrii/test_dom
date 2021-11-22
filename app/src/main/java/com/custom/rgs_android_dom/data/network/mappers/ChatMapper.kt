package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.CallInfoResponse
import com.custom.rgs_android_dom.data.network.responses.ChannelMemberResponse
import com.custom.rgs_android_dom.data.network.responses.ChatFileResponse
import com.custom.rgs_android_dom.data.network.responses.ChatMessageResponse
import com.custom.rgs_android_dom.domain.chat.models.*
import org.joda.time.LocalDateTime

object ChatMapper{

    private const val AVATAR_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

    fun responseToChatMessages(response: List<ChatMessageResponse>, userId: String): List<ChatMessageModel> {
        return response.map { messageResponse->
            ChatMessageModel(
                channelId = messageResponse.channelId,
                files = messageResponse.files?.map { fileResponse->
                    responseToChatFile(fileResponse, messageResponse.userId, messageResponse.createdAt.toLocalDateTime())
                },
                id = messageResponse.id ?: "",
                message = messageResponse.message.replace("\\n", "\n"),
                userId = messageResponse.userId,
                sender = if (messageResponse.userId == userId) Sender.ME else Sender.OPPONENT,
                createdAt = messageResponse.createdAt.toLocalDateTime(),
                type = messageResponse.type,
                member = null
            )
        }
    }

    fun responseToChatMessage(messageResponse: ChatMessageResponse, userId: String): ChatMessageModel {
        return ChatMessageModel(
            channelId = messageResponse.channelId,
            files = messageResponse.files?.map { fileResponse->
                responseToChatFile(fileResponse, messageResponse.userId, messageResponse.createdAt.toLocalDateTime())
            },
            id = messageResponse.id ?: "",
            message = messageResponse.message.replace("\\n", "\n"),
            userId = messageResponse.userId,
            sender = if (messageResponse.userId == userId) Sender.ME else Sender.OPPONENT,
            createdAt = messageResponse.createdAt.toLocalDateTime(),
            type = messageResponse.type,
            member = null
        )
    }

    fun responseToChannelMembers(response: List<ChannelMemberResponse>): List<ChannelMemberModel> {
        return response.map {
            val avatar = if (it.avatar?.isNotEmpty() == true) {
                "${AVATAR_ENDPOINT}/${it.avatar}"
            } else {
                ""
            }
            ChannelMemberModel(
                avatar = avatar,
                firstName = it.firstName ?: "",
                lastName = it.lastName ?: "",
                middleName = it.middleName ?: "",
                type = it.type ?: "",
                userId = it.userId ?: ""
            )
        }
    }

    fun responseToChatFile(fileResponse: ChatFileResponse, senderId: String, createdAt: LocalDateTime): ChatFileModel {
        return ChatFileModel(
            senderId = senderId,
            extension = fileResponse.extension,
            hasPreviewImage= fileResponse.hasPreviewImage,
            height = fileResponse.height,
            id = fileResponse.id,
            mimeType = fileResponse.mimeType,
            miniPreview = fileResponse.miniPreview,
            name = fileResponse.name,
            size = fileResponse.size,
            width = fileResponse.width,
            createdAt = createdAt
        )
    }

    fun responseToCallInfo(response: CallInfoResponse): CallInfoModel {
        return CallInfoModel(
            acceptedAt = response.acceptedAt,
            channelId = response.channelId ?: "",
            declinedAt = response.declinedAt,
            id = response.id ?: "",
            initiatorUserId = response.initiatorUserId ?: "",
            recipientUserId = response.recipientUserId ?: "",
            registeredAt = response.registeredAt,
            taskId = response.taskId ?: ""
        )
    }

}