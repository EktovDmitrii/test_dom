package com.custom.rgs_android_dom.domain.policies.models

data class PolicyModel(
    val id: String,
    val name: String,
    val logo: String? = "",
    val startsAt: String,
    val expiresAt: String
)
