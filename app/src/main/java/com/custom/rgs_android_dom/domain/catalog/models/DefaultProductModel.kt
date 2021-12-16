package com.custom.rgs_android_dom.domain.catalog.models

data class DefaultProductModel(
    val enabled: Boolean?,
    val productId: String?,
    val productName: String?,
    val serviceConsultingId: String?,
    val serviceConsultingName: String?,
    val tags: List<String>?
)