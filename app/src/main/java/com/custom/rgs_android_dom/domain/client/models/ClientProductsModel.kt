package com.custom.rgs_android_dom.domain.client.models

data class ClientProductsModel(
    val clientProducts: List<ClientProductModel>?,
    val index: Int = 0,
    val total: Int = 0
)
