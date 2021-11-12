package com.custom.rgs_android_dom.domain.chat.models


data class ChannelMemberModel(
    val avatar: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val type: String,
    val userId: String
)