package com.custom.rgs_android_dom.domain.property.view_states

import com.yandex.mapkit.geometry.Point

data class SelectAddressViewState(
    val isNextTextViewEnabled: Boolean,
    val propertyName: String,
    val locationPoint: Point?,
    val isMyLocationImageViewVisible: Boolean
)