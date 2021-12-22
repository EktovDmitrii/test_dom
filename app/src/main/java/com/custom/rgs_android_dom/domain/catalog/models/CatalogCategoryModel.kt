package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class CatalogCategoryModel(
    val id: String,
    val title: String,
    val icon: String,
    val productTags: List<String>,
    var products: List<ProductShortModel>,
    var subCategories: List<CatalogSubCategoryModel>
): Serializable