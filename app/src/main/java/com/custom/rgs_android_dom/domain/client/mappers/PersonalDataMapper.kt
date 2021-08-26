package com.custom.rgs_android_dom.domain.client.mappers

import android.util.Log
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.view_states.PersonalDataViewState
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask
import com.custom.rgs_android_dom.utils.formatTo

object PersonalDataMapper{

    fun from(client: ClientModel): PersonalDataViewState {
        val phoneMask = PhoneMaskHelper.getMaskForPhone(client.phone)
        return PersonalDataViewState(
            avatar = "",
            name = "${client.lastName} ${client.firstName} ${client.lastName}".trim(),
            birthday = client.birthDate?.formatTo() ?: "",
            gender = if (client.gender != null) { if (client.gender == Gender.MALE) "Мужчина" else "Женщина" } else "",
            passport = "",
            phone = client.phone.formatPhoneByMask(phoneMask, "#"),
            additionalPhone = "",
            email = ""
        )
    }

}