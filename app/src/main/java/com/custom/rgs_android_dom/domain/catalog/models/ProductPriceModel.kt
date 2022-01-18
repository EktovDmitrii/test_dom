package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class ProductPriceModel(
    val amount: Int?,
    val vatType: String?
): Serializable