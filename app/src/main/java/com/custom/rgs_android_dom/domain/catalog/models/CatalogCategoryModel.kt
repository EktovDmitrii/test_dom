package com.custom.rgs_android_dom.domain.catalog.models

data class CatalogCategoryModel(
    val id: String,
    val title: String,
    val productTags: List<String>,
    var subCategories: List<CatalogSubCategoryModel>
)