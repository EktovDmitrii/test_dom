package com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks

data class SecondaryCategoryModel(
    val title: String,
    val modelsWithPicture: List<SecondaryCategoryModelWithPicture>,
    val modelsWithoutPicture: List<SecondaryCategoryModelWithoutPicture>
)
