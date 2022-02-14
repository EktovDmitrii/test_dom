package com.custom.rgs_android_dom.domain.policies.models

import org.joda.time.DateTime

data class PolicyModel (
    val id: String,
    val logo: String? = null,
    val productTitle: String,
    val productDescription: String,
    val address: String,
    val includedProducts: List<Any>?,
    val policySeriesAndNumber: String,
    val startsAt: DateTime? = null,
    val expiresAt: DateTime? = null
)