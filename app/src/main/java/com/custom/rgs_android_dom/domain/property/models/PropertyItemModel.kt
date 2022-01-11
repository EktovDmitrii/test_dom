package com.custom.rgs_android_dom.domain.property.models

import java.io.Serializable

data class PropertyItemModel(
    val address: PropertyAddressModel?,
    val clientId: String,
    val comment: String,
    var documents: List<PropertyDocument>,
    val id: String,
    val isOwn: Boolean?,
    val isRent: Boolean?,
    val isTemporary: Boolean?,
    val name: String,
    val status: String,
    val totalArea: Float?,
    val type: PropertyType
) : Serializable
