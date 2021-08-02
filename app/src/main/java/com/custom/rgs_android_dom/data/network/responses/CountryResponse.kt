package com.custom.rgs_android_dom.data.network.responses

data class CountryResponse(
    val id: Long,
    val name: String,
    val letterCode: String,
    val numberCode: String,
    val mask: String
)