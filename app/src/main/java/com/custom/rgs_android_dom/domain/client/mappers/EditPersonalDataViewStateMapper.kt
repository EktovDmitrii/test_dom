package com.custom.rgs_android_dom.domain.client.mappers

import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.catalog.models.ClientProductModel
import com.custom.rgs_android_dom.domain.client.view_states.EditPersonalDataViewState
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask
import com.custom.rgs_android_dom.utils.formatTo

object EditPersonalDataViewStateMapper {

    fun from(client: ClientModel, clientProducts: List<ClientProductModel>): EditPersonalDataViewState {

        val phoneMask = PhoneMaskHelper.getMaskForPhone(client.phone)

        val passport = client.documents?.find { it.type == ClientMapper.DOCTYPE_NATIONAL_PASSPORT }

        var secondPhone = client.contacts?.find {
            it.type == ClientMapper.CONTACT_TYPE_PHONE && it.contact != client.phone
        }?.contact ?: ""

        val secondPhoneId = client.contacts?.find {
            it.type == ClientMapper.CONTACT_TYPE_PHONE && it.contact != client.phone
        }?.id ?: ""

        if (secondPhone.isNotEmpty()) {
            val secondPhoneMask = PhoneMaskHelper.getMaskForPhone(secondPhone)
            secondPhone = secondPhone.formatPhoneByMask(secondPhoneMask, "#")
        }

        val emailId = client.contacts?.find {
            it.type == ClientMapper.CONTACT_TYPE_EMAIL
        }?.id ?: ""

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
            docId = passport?.id ?: "",
            docNumber = passport?.number ?: "",
            isDocNumberSaved = passport?.number?.isNotEmpty() == true,
            docSerial = passport?.serial ?: "",
            isDocSerialSaved = passport?.serial?.isNotEmpty() == true,
            secondPhone = secondPhone,
            secondPhoneId = secondPhoneId,
            isSecondPhoneSaved = secondPhone.isNotEmpty(),
            email = client.contacts?.find { it.type == "email" }?.contact ?: "",
            emailId = emailId,
            agentCode = client.agent?.code,
            agentPhone = client.agent?.phone,
            hasProducts = clientProducts.isNotEmpty()
        )
    }

}