package com.custom.rgs_android_dom.domain.property.view_states

data class SelectAddressViewState(
    val isNextTextViewEnabled: Boolean,
    val propertyName: String,
    val isMyLocationImageViewVisible: Boolean,
    val updatePropertyNameEditText: Boolean,
    val propertyAddress: String
)