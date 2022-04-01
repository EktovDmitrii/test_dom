package com.custom.rgs_android_dom.domain.policies.models

import com.custom.rgs_android_dom.domain.catalog.models.ServiceShortModel
import org.joda.time.DateTime

data class PolicyModel (
    val id: String,
    val productId: String,
    val logo: String? = null,
    val productTitle: String,
    val productDescription: String,
    val address: String?,
    val includedProducts: List<ServiceShortModel>? = listOf(),
    val policySeriesAndNumber: String,
    val clientName: String,
    val startsAt: DateTime? = null,
    val expiresAt: DateTime? = null,
    val isActive: Boolean = true
)