package com.custom.rgs_android_dom.domain.catalog.models

data class CatalogNodeModel(
    val id: String,
    val code: String,
    val iconLink: String,
    val name: String,
    val parentNodeId: String,
    val productTags: List<String>,
    val title: String
)