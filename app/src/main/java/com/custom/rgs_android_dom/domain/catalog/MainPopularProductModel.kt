package com.custom.rgs_android_dom.domain.catalog

import com.custom.rgs_android_dom.domain.catalog.models.ProductPriceModel


data class MainPopularProductModel(
    val id: String,
    val logoLarge: String?,
    val logoSmall: String?,
    val name: String?,
    val productTags: List<String>?,
    val price: ProductPriceModel?
)