package com.custom.rgs_android_dom.domain.client.exceptions

data class ValidateFieldModel(
    val fieldName: ClientField,
    val errorMessage: String
)
