package com.custom.rgs_android_dom.domain.client.view_states

import com.custom.rgs_android_dom.domain.client.models.Gender
import org.joda.time.LocalDateTime

data class FillClientViewState(
    val phone: String,
    val isOpenCodeAgendFields: Boolean = false,
    val name: String? = null,
    val surname: String? = null,
    //val birthday: LocalDateTime? = null,
    val birthday: String? = null,
    val gender: Gender? = null,
    val agentCode: String? = null,
    val agentPhone: String? = null,
    val agentPhoneValid: Boolean = false,
    val isValidate: Boolean = false
){
    fun onNameChanged(name: String): FillClientViewState {
        if (name.isNotEmpty()){
            return this.copy(name = name)
        } else {
            return this.copy(name = null)
        }
    }
}

