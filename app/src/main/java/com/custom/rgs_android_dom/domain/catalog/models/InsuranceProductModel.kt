package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class InsuranceProductModel(
    val productId: String,
    val programId: String?
): Serializable