package com.custom.rgs_android_dom.domain.property.models

import java.io.Serializable

data class PropertyDocument(
    val link: String,
    var name: String
) : IPropertyDocument, Serializable
