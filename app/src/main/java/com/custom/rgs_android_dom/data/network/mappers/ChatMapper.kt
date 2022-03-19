package com.custom.rgs_android_dom.data.network.mappers

import android.graphics.BitmapFactory
import android.util.Base64
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.*
import com.custom.rgs_android_dom.domain.chat.models.*
import com.google.gson.internal.LinkedTreeMap
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import java.lang.IllegalArgumentException

object ChatMapper{

    private const val STORE_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

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
                widget = messageResponse.details?.let { widgetModel(it) }
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
            widget = messageResponse.details?.let { widgetModel(it) }
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

    fun responseToCallConnection(response: CallConnectionResponse): CallConnectionModel {
        return CallConnectionModel(
            channelId = response.channelId ?: "",
            id = response.id ?: "",
            initiatorUserId = response.initiatorUserId ?: "",
            recipientUserId = response.recipientUserId ?: "",
            taskId = response.taskId ?: ""
        )
    }

    private fun widgetModel(details: WidgetResponse): WidgetModel {
        return when (details.widgetType) {
            WidgetType.GeneralInvoicePayment.name -> WidgetModel.WidgetAdditionalInvoiceModel(
                amount = details.amount,
                invoiceId = details.invoiceId,
                items = details.items?.map { WidgetAdditionalInvoiceItemModel(
                    amount = it.amount,
                    name = it.name,
                    price = it.price,
                    quantity = it.quantity
                ) },
                orderId = details.orderId,
                paymentUrl = details.paymentUrl,
                serviceLogo = if (!details.serviceLogo.isNullOrEmpty()) "$STORE_ENDPOINT/${details.serviceLogo}" else "",
                serviceName = details.serviceName,
                widgetType = WidgetType.GeneralInvoicePayment
            )
            WidgetType.OrderComplexProduct.name -> WidgetModel.WidgetOrderComplexProductModel(
                clientServiceId = details.clientServiceId,
                deliveryTime = details.deliveryTime,
                icon = "$STORE_ENDPOINT/${details.icon}",
                name = details.name,
                objAddr = details.objAddr,
                objId = details.objId,
                objName = details.objName,
                objType = details.objType,
                objPhotoLink = details.objPhotoLink,
                orderDate = details.orderDate,
                orderTime = details.orderTime.let { OrderTimeModel(from = it?.from, to = it?.to) },
                widgetType = WidgetType.OrderComplexProduct
            )
            WidgetType.OrderDefaultProduct.name -> WidgetModel.WidgetOrderDefaultProductModel(
                deliveryTime = details.deliveryTime,
                icon = "$STORE_ENDPOINT/${details.icon}",
                name = details.name,
                objAddr = details.objAddr,
                objId = details.objId,
                objName = details.objName,
                objPhotoLink = details.objPhotoLink,
                objType = details.objType,
                orderDate = details.orderDate,
                orderTime = details.orderTime.let { OrderTimeModel(from = it?.from, to = it?.to) },
                productId = details.productId,
                widgetType = WidgetType.OrderDefaultProduct,
                price = if (details.price is Double) details.price.toInt() else null
            )
            WidgetType.Product.name -> WidgetModel.WidgetOrderProductModel(
                avatar = "$STORE_ENDPOINT/${details.avatar}",
                description = details.description,
                name = details.name,
                price = if (details.price != null && details.price !is Double) WidgetPriceModel(amount = ((details.price as LinkedTreeMap<*, *>)["Amount"] as Double?)?.toInt(), vatType = details.price["VatType"].toString(), fix = details.price["Fix"] == true) else null,
                productId = details.productId,
                widgetType = WidgetType.Product
            )
            else -> throw IllegalArgumentException("wrong type widget type: ${details.widgetType}")
        }
    }
}