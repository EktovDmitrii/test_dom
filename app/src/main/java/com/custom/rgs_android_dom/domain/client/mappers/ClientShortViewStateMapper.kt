package com.custom.rgs_android_dom.domain.client.mappers

import com.custom.rgs_android_dom.domain.client.view_states.ClientShortViewState
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask

object ClientShortViewStateMapper {

    fun from(clientModel: ClientModel): ClientShortViewState {
        val phoneMask = PhoneMaskHelper.getMaskForPhone(clientModel.phone)

        return ClientShortViewState(
            firstName = clientModel.firstName,
            lastName = clientModel.lastName,
            phone = clientModel.phone.formatPhoneByMask(phoneMask, "#"))
    }
}