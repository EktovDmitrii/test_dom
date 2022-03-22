package com.custom.rgs_android_dom.domain.client.view_states

data class AgentViewState(
    var isEditAgentButtonVisible: Boolean = true,
    var agentCode: String = "",
    var agentPhone: String = ""
)