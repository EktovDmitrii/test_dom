package com.custom.rgs_android_dom.domain.purchase_service

import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import java.io.Serializable

data class PropertyListModel(
    val propertyList: List<PropertyItemModel>
) : Serializable
