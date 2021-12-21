package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.SecondaryCategoryModel
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.TertiaryCategoryModel

data class ProductViewState(
    val primaryCategories: List<Any> = listOf(),
    val secondaryCategories: List<SecondaryCategoryModel> = listOf(),
    val tertiaryCategories: List<TertiaryCategoryModel> = listOf()
)



