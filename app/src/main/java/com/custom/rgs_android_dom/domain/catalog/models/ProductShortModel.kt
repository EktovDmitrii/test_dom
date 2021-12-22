package com.custom.rgs_android_dom.domain.catalog.models

data class ProductShortModel(
    val id: String,
    val type: String,
    val title: String,
    val code: String,
    val versionId: String,
    val name: String,
    val price: Int
)