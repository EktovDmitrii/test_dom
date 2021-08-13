package com.custom.rgs_android_dom.domain.profile.models

data class ClientContact (
    val id: String,
    val contact: String?,
    val preferred: Boolean?,
    val type: String?,
    val verified: Boolean?
)