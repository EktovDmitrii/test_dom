package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.LocalDateTime

sealed class ChatItemModel constructor(
    open val channelId: String = "",
    open val id: String? = null
)

data class ChatMessageModel(
    override val channelId: String,
    var files: List<ChatFileModel>? = null,
    var fileIds: List<Int>? = null,
    override val id: String,
    val message: String,
    val userId: String,
    var sender: Sender,
    val createdAt: LocalDateTime
) : ChatItemModel()

data class ChatDateDividerModel(
    val date: String
) : ChatItemModel()


enum class Sender {ME, OPPONENT}