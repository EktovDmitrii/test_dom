package com.custom.rgs_android_dom.domain.property.models

import java.io.Serializable

data class PropertyItemModel(
    val address: PropertyAddressModel?,
    val clientId: String,
    val comment: String,
    var documents: MutableList<PropertyDocument>,
    val id: String,
    val isOwn: Boolean?,
    val isRent: Boolean?,
    val isTemporary: Boolean?,
    val name: String,
    val photoLink: String?,
    val status: String,
    val totalArea: Float?,
    val type: PropertyType,
    val isEmpty: Boolean = false
) : Serializable {

    companion object {
        fun empty() = PropertyItemModel(
            address = null,
            clientId = "",
            comment = "",
            documents = mutableListOf(),
            id = "",
            isOwn = null,
            isTemporary = null,
            isRent = null,
            name = "",
            photoLink = null,
            status = "",
            totalArea = null,
            type = PropertyType.UNDEFINED,
            isEmpty = true
        )
    }

}
