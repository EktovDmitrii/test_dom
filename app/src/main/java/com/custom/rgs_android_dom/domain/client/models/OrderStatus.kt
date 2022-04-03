package com.custom.rgs_android_dom.domain.client.models

enum class OrderStatus(val key: String, val value: String) {
    DRAFT("draft", "Создан"),
    CONFIRMED("confirmed", "Подтвержден"),
    ACTIVE("active", "Активный"),
    RESOLVED("resolved", "Завершен"),
    CANCELLED("cancelled", "Отменен"),
    NOT_FOUND("", "")
}
