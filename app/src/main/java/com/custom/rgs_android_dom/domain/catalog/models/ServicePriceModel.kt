package com.custom.rgs_android_dom.domain.catalog.models

data class ServicePriceModel(
    val amount: Int,
    val vatType: String?,
    val fix: Boolean
)