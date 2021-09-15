package com.custom.rgs_android_dom.domain.chat.models

data class ChatMessageModel(
    val channelId: String,
    val files: List<ChatFileModel>,
    val id: String,
    val message: String,
    val userId: String,
    val sender: Sender
) {

    enum class Sender {ME, OPPONENT}

}