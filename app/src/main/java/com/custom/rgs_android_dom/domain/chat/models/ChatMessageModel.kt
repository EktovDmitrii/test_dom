package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.LocalDateTime

data class ChatMessageModel(
    override val channelId: String,
    var files: List<ChatFileModel>? = null,
    var fileIds: List<Int>? = null,
    override val id: String,
    val message: String,
    val userId: String,
    var sender: Sender,
    val createdAt: LocalDateTime,
    val type: String,
    var member: ChannelMemberModel?,
    val widget: WidgetModel?
) : ChatItemModel()