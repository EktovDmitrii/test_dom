package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class ServiceDurationModel(
    val unitType: ProductUnitType,
    val units: Int
): Serializable