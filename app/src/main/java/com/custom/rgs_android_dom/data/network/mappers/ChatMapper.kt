package com.custom.rgs_android_dom.data.network.mappers

import android.graphics.BitmapFactory
import android.util.Base64
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.CallInfoResponse
import com.custom.rgs_android_dom.data.network.responses.ChannelMemberResponse
import com.custom.rgs_android_dom.data.network.responses.ChatFileResponse
import com.custom.rgs_android_dom.data.network.responses.ChatMessageResponse
import com.custom.rgs_android_dom.domain.chat.models.*
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

object ChatMapper{

    private const val AVATAR_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

    fun responseToChatMessages(response: List<ChatMessageResponse>, userId: String): List<ChatMessageModel> {
        return response.map { messageResponse->
            ChatMessageModel(
                channelId = messageResponse.channelId,
                files = messageResponse.files?.map { fileResponse->
                    responseToChatFile(fileResponse, messageResponse.userId, messageResponse.createdAt.withZone(DateTimeZone.getDefault()).toLocalDateTime())
                },
                id = messageResponse.id ?: "",
                message = messageResponse.message.replace("\\n", "\n"),
                userId = messageResponse.userId,
                sender = if (messageResponse.userId == userId) Sender.ME else Sender.OPPONENT,
                createdAt = messageResponse.createdAt.withZone(DateTimeZone.getDefault()).toLocalDateTime(),
                type = messageResponse.type,
                member = null,
                widget = messageResponse.details?.let {
                    val avatar = if (it.avatar.isNotEmpty()) {
                        "${AVATAR_ENDPOINT}/${it.avatar}"
                    } else {
                        ""
                    }
                    WidgetModel(
                        avatar = avatar,
                        description = it.description,
                        name = it.name,
                        price = WidgetPriceModel(amount = it.price.amount,vatType = it.price.vatType),
                        productId = it.productId,
                        widgetType = it.widgetType
                    )
                }
            )
        }
    }

    fun responseToChatMessage(messageResponse: ChatMessageResponse, userId: String): ChatMessageModel {
        return ChatMessageModel(
            channelId = messageResponse.channelId,
            files = messageResponse.files?.map { fileResponse->
                responseToChatFile(fileResponse, messageResponse.userId, messageResponse.createdAt.withZone(DateTimeZone.getDefault()).toLocalDateTime())
            },
            id = messageResponse.id ?: "",
            message = messageResponse.message.replace("\\n", "\n"),
            userId = messageResponse.userId,
            sender = if (messageResponse.userId == userId) Sender.ME else Sender.OPPONENT,
            createdAt = messageResponse.createdAt.withZone(DateTimeZone.getDefault()).toLocalDateTime(),
            type = messageResponse.type,
            member = null,
            widget = messageResponse.details?.let {
                val avatar = if (it.avatar.isNotEmpty()) {
                    "${AVATAR_ENDPOINT}/${it.avatar}"
                } else {
                    ""
                }
                WidgetModel(
                    avatar = avatar,
                    description = it.description,
                    name = it.name,
                    price = WidgetPriceModel(amount = it.price.amount,vatType = it.price.vatType),
                    productId = it.productId,
                    widgetType = it.widgetType
                )
            }
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
        val miniPreview = if (fileResponse.miniPreview != null) {
            val miniPreviewString = Base64.decode(fileResponse.miniPreview, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(miniPreviewString, 0, miniPreviewString.size)
        } else {
            null
        }

        return ChatFileModel(
            senderId = senderId,
            extension = fileResponse.extension,
            hasPreviewImage= fileResponse.hasPreviewImage,
            height = fileResponse.height,
            id = fileResponse.id,
            mimeType = fileResponse.mimeType,
            miniPreview = miniPreview,
            preview = "${BuildConfig.BASE_URL}/api/chat/users/me/files/${fileResponse.id}/preview",
            name = fileResponse.name,
            size = fileResponse.size,
            width = fileResponse.width,
            createdAt = createdAt
        )
    }

    fun responseToCallInfo(response: CallInfoResponse): CallInfoModel {
        return CallInfoModel(
            channelId = response.channelId ?: "",
            id = response.id ?: "",
            initiatorUserId = response.initiatorUserId ?: "",
            recipientUserId = response.recipientUserId ?: "",
            taskId = response.taskId ?: ""
        )
    }

}