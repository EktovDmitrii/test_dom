package com.custom.rgs_android_dom.domain.property.models

import java.io.Serializable

data class PropertyDocumentsModel(
    val propertyId: String,
    val propertyDocuments: List<PropertyDocument>
) : Serializable
