package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class ProductShortModel(
    val id: String,
    val type: String,
    val title: String,
    val code: String,
    val icon: String,
    val logoSmall: String,
    val logoMiddle: String,
    val logoLarge: String,
    val versionId: String,
    val name: String,
    val price: Int,
    val tags: List<String>,
    val defaultProduct: Boolean = false
) : Serializable {

    companion object {
        fun getEmptyProduct() = ProductShortModel(
            id = "",
            type = "",
            title = "",
            code = "",
            versionId = "",
            name = "",
            price = 0,
            tags = emptyList(),
            icon = "",
            logoSmall = "",
            logoMiddle = "",
            logoLarge = "",
        )
    }

}