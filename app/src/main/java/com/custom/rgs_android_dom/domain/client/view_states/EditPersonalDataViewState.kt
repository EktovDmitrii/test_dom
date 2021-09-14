package com.custom.rgs_android_dom.domain.client.view_states

import com.custom.rgs_android_dom.domain.client.models.Gender

data class EditPersonalDataViewState(
    val lastName: String = "",
    val isLastNameSaved: Boolean = false,
    val firstName: String = "",
    val isFirstNameSaved: Boolean = false,
    val middleName: String = "",
    val isMiddleNameSaved: Boolean = false,
    val birthday: String = "",
    val isBirthdaySaved: Boolean = false,
    val gender: Gender? = null,
    val isGenderSaved: Boolean = false,
    val docNumber: String = "",
    val isDocNumberSaved: Boolean = false,
    val docSerial: String = "",
    val isDocSerialSaved: Boolean = false,
    val phone: String = "",
    val isPhoneSaved: Boolean = false,
    val secondPhone: String = "",
    val secondPhoneId: String = "",
    val isSecondPhoneSaved: Boolean = false,
    val isSecondPhoneValid: Boolean = false,
    val email: String = "",
    val wasSecondPhoneEdited: Boolean = false,
    val emailId: String = "",
    val wasEmailEdited: Boolean = false,
    val agentCode: String? = null,
    val agentPhone: String? = null
)
