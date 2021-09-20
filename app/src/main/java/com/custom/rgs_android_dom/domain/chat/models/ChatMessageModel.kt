package com.custom.rgs_android_dom.domain.chat.models

data class ChatMessageModel(
    val channelId: String,
    var files: List<ChatFileModel>? = null,
    var fileIds: List<Int>? = null,
    val id: String,
    val message: String,
    val userId: String,
    var sender: Sender
) {

    enum class Sender {ME, OPPONENT}

}