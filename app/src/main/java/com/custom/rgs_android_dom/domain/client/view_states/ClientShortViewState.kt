package com.custom.rgs_android_dom.domain.client.view_states

data class ClientShortViewState(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val hasAgentInfo: Boolean,
    val isOpdSigned: Boolean,
    val avatar: String
)