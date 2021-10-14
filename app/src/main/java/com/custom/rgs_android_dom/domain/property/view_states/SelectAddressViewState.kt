package com.custom.rgs_android_dom.domain.property.view_states

import com.custom.rgs_android_dom.domain.address.models.AddressItemModel

data class SelectAddressViewState(
    val isNextTextViewEnabled: Boolean,
    val propertyName: String,
    val isMyLocationImageViewVisible: Boolean,
    val updatePropertyNameEditText: Boolean,
    val propertyAddress: AddressItemModel
)