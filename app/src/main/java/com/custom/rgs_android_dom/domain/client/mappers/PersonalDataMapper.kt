package com.custom.rgs_android_dom.domain.client.mappers

import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.models.UserDetailsModel
import com.custom.rgs_android_dom.domain.client.view_states.PersonalDataViewState
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask
import com.custom.rgs_android_dom.utils.formatTo

object PersonalDataMapper{

    fun from(client: ClientModel, userDetails: UserDetailsModel): PersonalDataViewState {
        val phoneMask = PhoneMaskHelper.getMaskForPhone(client.phone)

        val passport = client.documents?.find { it.type == ClientMapper.DOCTYPE_NATIONAL_PASSPORT }
        val passportString = if (passport != null){
            "${passport.serial} ${passport.number}"
        } else ""

        var secondPhone = client.contacts?.find {
            it.type == ClientMapper.CONTACT_TYPE_PHONE && it.contact != client.phone
        }?.contact ?: ""

        if (secondPhone.isNotEmpty()){
            val secondPhoneMask = PhoneMaskHelper.getMaskForPhone(secondPhone)
            secondPhone = secondPhone.formatPhoneByMask(secondPhoneMask, "#")
        }

        return PersonalDataViewState(
            avatar = userDetails.avatarUrl ?: "",
            name = "${client.lastName} ${client.firstName} ${client.middleName}".trim(),
            birthday = client.birthDate?.formatTo(DATE_PATTERN_DATE_ONLY) ?: "",
            gender = if (client.gender != null) {
                if (client.gender == Gender.MALE) "Мужчина" else "Женщина"
            } else "",
            passport = passportString,
            phone = client.phone.formatPhoneByMask(phoneMask, "#"),
            additionalPhone = secondPhone,
            email = client.contacts?.find { it.type == "email" }?.contact ?: ""
        )
    }

}