package com.custom.rgs_android_dom.domain.catalog.models

enum class ProductUnitType(val description: String) {
    MONTHS("месяцев после покупки"),
    DAYS("дней после покупки"),
    YEARS("год после покупки"),
    UNKNOWN("неизвестно")
}
