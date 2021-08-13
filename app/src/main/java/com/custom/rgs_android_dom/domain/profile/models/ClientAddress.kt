package com.custom.rgs_android_dom.domain.profile.models

data class ClientAddress (
    val id: String,
    val address: String?,
    val lat: Int?,
    val long: Int?,
    val preferred: Boolean?,
    val type: String?
)