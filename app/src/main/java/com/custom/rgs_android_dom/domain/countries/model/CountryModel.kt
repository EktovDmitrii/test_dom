package com.custom.rgs_android_dom.domain.countries.model

data class CountryModel(
    val id: Long,
    val name: String,
    val image: Int,
    val letterCode: String,
    val numberCode: String,
    val isCurrent: Boolean,
    val mask: String
)