package com.custom.rgs_android_dom.domain.error.model

data class MSDErrorModel(
    val code: String,
    val messageKey: String,
    val defaultMessage: String
)