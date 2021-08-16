package com.custom.rgs_android_dom.domain.profile

import com.custom.rgs_android_dom.domain.profile.models.Gender
import org.joda.time.LocalDate

data class ProfileViewState(
    val phone: String,
    val isOpenCodeAgendFields: Boolean = false,
    val name: String? = null,
    val surname: String? = null,
    val birthday: LocalDate? = null,
    val gender: Gender? = null,
    val agentCode: String? = null,
    val agentPhone: String? = null,
    val agentPhoneValid: Boolean = false,
    val isValidate: Boolean = false
){
    fun onNameChanged(name: String): ProfileViewState {
        if (name.isNotEmpty()){
            return this.copy(name = name)
        } else {
            return this.copy(name = null)
        }
    }
}

