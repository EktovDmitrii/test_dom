package com.custom.rgs_android_dom.domain.property.details.view_states

import android.net.Uri
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument

data class PropertyDetailsViewState(
    val name: String,
    val type: String,
    val address: AddressItemModel,
    val entrance: String,
    val corpus: String,
    val floor: String,
    val flat: String,
    val isOwn: String?,
    val isRent: String?,
    val isTemporary: String?,
    val photoLink: String?,
    val totalArea: String,
    val comment: String,
    val isAddTextViewEnabled: Boolean,
    val documents: List<Uri> = listOf(),
    val documentsFormatted: List<PropertyDocument> = listOf(),
)
