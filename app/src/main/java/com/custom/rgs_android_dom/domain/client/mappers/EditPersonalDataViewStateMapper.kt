package com.custom.rgs_android_dom.domain.client.mappers

import android.util.Log
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.view_states.EditPersonalDataViewState
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask
import com.custom.rgs_android_dom.utils.formatTo

object EditPersonalDataViewStateMapper {

    fun from(client: ClientModel): EditPersonalDataViewState {
        val phoneMask = PhoneMaskHelper.getMaskForPhone(client.phone)
        return EditPersonalDataViewState(
            lastName = client.lastName,
            isLastNameSaved = client.lastName.isNotEmpty(),
            firstName = client.firstName,
            isFirstNameSaved = client.firstName.isNotEmpty(),
            middleName = client.middleName ?: "",
            isMiddleNameSaved = client.middleName?.isNotEmpty() == true,
            birthday = client.birthDate?.formatTo(DATE_PATTERN_DATE_ONLY) ?: "",
            isBirthdaySaved = client.birthDate != null,
            gender = client.gender,
            isGenderSaved = client.gender != null,
            phone = client.phone.formatPhoneByMask(phoneMask, "#"),
            isPhoneSaved = client.phone.isNotEmpty(),
            docNumber = client.docNumber ?: "",
            isDocNumberSaved = client.docNumber?.isNotEmpty() == true,
            docSerial = client.docSerial ?: "",
            isDocSerialSaved = client.docSerial?.isNotEmpty() == true,
            secondPhone = client.secondPhone ?: "",
            isSecondPhoneSaved = client.secondPhone?.isNotEmpty() == true,
            email = client.contacts?.find { it.type == "email" }?.contact ?: "",
            isEmailSaved = client.contacts?.find { it.type == "email" }?.contact != null
        )
    }

}