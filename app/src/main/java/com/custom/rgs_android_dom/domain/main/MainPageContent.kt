package com.custom.rgs_android_dom.domain.main

import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel

data class MainPageContent(
    val services: List<ProductShortModel>,
    val products: List<ProductShortModel>,
    val categories: List<CatalogCategoryModel>,
)