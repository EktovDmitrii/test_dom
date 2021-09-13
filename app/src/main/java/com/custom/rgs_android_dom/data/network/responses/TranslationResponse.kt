package com.custom.rgs_android_dom.data.network.responses

data class TranslationListResponse(
    val list: List<TranslationResponse>
)

data class TranslationResponse(
    val key: String,
    val value: String
)