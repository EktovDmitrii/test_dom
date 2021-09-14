package com.custom.rgs_android_dom.domain.chat.models

data class ChatMessageModel(
    val message: String,
    val sender: Sender
) {

    enum class Sender {ME, OPPONENT}

}