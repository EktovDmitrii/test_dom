package com.custom.rgs_android_dom.ui.policies.insurant

data class InsurantViewState(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: String,
    val policy: String,
    val isNextEnabled: Boolean = false
)