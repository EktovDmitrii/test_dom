package com.custom.rgs_android_dom.domain.catalog.models

data class CatalogSubCategoryModel(
    val id: String,
    val title: String,
    val parentCategoryId: String,
    val icon: String,
    val productTags: List<String>,
    var productsCount: Int
)