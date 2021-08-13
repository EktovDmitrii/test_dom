package com.custom.rgs_android_dom.domain.profile.models

data class ClientDocument (
    val id: String,
    val expireAt: String?,
    val fileIds: List<String>?,
    val issuedAt: String?,
    val issuedBy: String?,
    val number: String?,
    val preferred: Boolean?,
    val serial: String?,
    val type: String?
)
