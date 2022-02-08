package com.custom.rgs_android_dom.domain.client.view_states

data class PersonalDataViewState(
    val avatar: String,
    val name: String,
    val birthday: String,
    val gender: String,
    val passport: String,
    val phone: String,
    val additionalPhone: String,
    val email: String
)