package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class CatalogCategoryModel(
    val id: String,
    val title: String,
    val name: String,
    val logoSmall: String,
    val logoMiddle: String,
    val logoLarge: String,
    val isPrimary: Boolean = false,
    val productTags: List<String>,
    var products: List<ProductShortModel>,
    var subCategories: List<CatalogSubCategoryModel>
): Serializable