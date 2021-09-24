package com.custom.rgs_android_dom.domain.property.details.exceptions

class ValidatePropertyException(val field: PropertyField, val errorMessage: String): RuntimeException(errorMessage)

enum class PropertyField {
    ADDRESS
}