package com.custom.rgs_android_dom.data.network.mappers

import android.graphics.BitmapFactory
import android.util.Base64
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.*
import com.custom.rgs_android_dom.domain.chat.models.*
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

object ChatMapper{

    private const val STORE_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"
    private const val WIDGET_TYPE_ORDER_COMPLEX_PRODUCT = "OrderComplexProduct"
    private const val WIDGET_TYPE_ORDER_PRODUCT = "Product"
    private const val WIDGET_TYPE_ORDER_DEFAULT_PRODUCT = "OrderDefaultProduct"
    private const val WIDGET_TYPE_ADDITIONAL_INVOICE = "GeneralInvoicePayment"

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
                widget = widgetModel(messageResponse.details)
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
            widget = widgetModel(messageResponse.details)
        )
    }

    fun responseToChannelMembers(response: List<ChannelMemberResponse>): List<ChannelMemberModel> {
        return response.map {
            val avatar = if (it.avatar?.isNotEmpty() == true) {
                "${STORE_ENDPOINT}/${it.avatar}"
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

    private fun widgetModel(details: WidgetResponse?): WidgetModel? {
        details?.let {
            when (it.widgetType) {
                WIDGET_TYPE_ORDER_COMPLEX_PRODUCT -> {
                    val icon = if (!it.icon.isNullOrEmpty()) {
                        "${STORE_ENDPOINT}/${it.icon}"
                    } else {
                        ""
                    }
                    return WidgetModel.WidgetOrderComplexProductModel(
                        clientServiceId = it.clientServiceId,
                        deliveryTime = it.deliveryTime,
                        icon = icon,
                        name = it.name,
                        objAddr = it.objAddr,
                        objId = it.objId,
                        objName = it.objName,
                        orderDate = it.orderDate,
                        orderTime = OrderTimeModel(
                            from = it.orderTime?.from,
                            to = it.orderTime?.to
                        ),
                        widgetType = it.widgetType
                    )
                }
                WIDGET_TYPE_ORDER_PRODUCT -> {
                    val avatar = if (!it.avatar.isNullOrEmpty()) {
                        "${STORE_ENDPOINT}/${it.avatar}"
                    } else {
                        ""
                    }
                    return WidgetModel.WidgetOrderProductModel(
                        avatar = avatar,
                        description = it.description,
                        name = it.name,
                        price = WidgetPriceModel(amount = it.price?.amount, vatType = it.price?.vatType),
                        productId = it.productId,
                        widgetType = it.widgetType
                    )
                }
                WIDGET_TYPE_ORDER_DEFAULT_PRODUCT -> {
                    val icon = if (!it.icon.isNullOrEmpty()) {
                        "${STORE_ENDPOINT}/${it.icon}"
                    } else {
                        ""
                    }
                    val objPhotoLink = if (!it.objPhotoLink.isNullOrEmpty()) {
                        "${STORE_ENDPOINT}/${it.objPhotoLink}"
                    } else {
                        ""
                    }
                    return WidgetModel.WidgetOrderDefaultProductModel(
                        deliveryTime = it.deliveryTime,
                        icon = icon,
                        name = it.name,
                        objAddr = it.objAddr,
                        objId = it.objId,
                        objName = it.objName,
                        objPhotoLink = objPhotoLink,
                        objType = it.objType,
                        orderDate = it.orderDate,
                        orderTime = OrderTimeModel(
                            from = it.orderTime?.from,
                            to = it.orderTime?.to
                        ),
                        productId = it.productId,
                        widgetType = it.widgetType,
                        price1 = it.price1
                    )
                }
                WIDGET_TYPE_ADDITIONAL_INVOICE -> return WidgetModel.WidgetAdditionalInvoiceModel(
                    amount = it.amount,
                    invoiceId = it.invoiceId,
                    items = it.items?.map {
                        WidgetAdditionalInvoiceItemModel(
                            amount = it.amount,
                            name = it.name,
                            price = it.price,
                            quantity = it.quantity
                        )
                    } ?: listOf(),
                    orderId = it.orderId,
                    paymentUrl = it.paymentUrl,
                    serviceLogo = it.serviceLogo,
                    serviceName = it.serviceName,
                    widgetType = it.widgetType
                )
                else -> return null
            }

        } ?: return null
    }

}