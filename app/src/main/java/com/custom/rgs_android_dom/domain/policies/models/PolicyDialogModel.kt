package com.custom.rgs_android_dom.domain.policies.models

import java.io.Serializable

data class PolicyDialogModel(
    val failureMessage: Failure? = null,
    val showLoader: Boolean = false,
    val bound: Boolean? = null
): Serializable


