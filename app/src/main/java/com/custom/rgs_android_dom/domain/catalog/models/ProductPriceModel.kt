package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class ProductPriceModel(
    var amount: Int?,
    val vatType: String?,
    val fix: Boolean
): Serializable