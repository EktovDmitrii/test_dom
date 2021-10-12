package com.custom.rgs_android_dom.domain.property.details.view_states

data class PropertyDetailsViewState(
    val name: String,
    val type: String,
    val address: String,
    val entrance: String,
    val corpus: String,
    val floor: String,
    val flat: String,
    val isOwn: String?,
    val isRent: String?,
    val isTemporary: String?,
    val totalArea: String,
    val comment: String,
    val isAddTextViewEnabled: Boolean,
    val updatePropertyAddressEditText: Boolean
)
