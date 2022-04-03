package com.custom.rgs_android_dom.domain.policies.models

import org.joda.time.DateTime

abstract class PolicyViewholderModel

data class PolicyShortModel(
    val id: String,
    val contractId: String,
    val name: String,
    val logo: String? = null,
    val startsAt: DateTime? = null,
    val expiresAt: DateTime? = null
) : PolicyViewholderModel()

data class PolicyDividerModel(
    val text: String
) : PolicyViewholderModel()