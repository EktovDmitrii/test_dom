package com.custom.rgs_android_dom.domain.client.mappers

import android.util.Log
import com.custom.rgs_android_dom.domain.client.view_states.ClientShortViewState
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.UserDetailsModel
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask

object ClientShortViewStateMapper {

    fun from(clientModel: ClientModel, userDetails: UserDetailsModel): ClientShortViewState {
        val phoneMask = PhoneMaskHelper.getMaskForPhone(clientModel.phone)
        val hasAgentInfo = clientModel.agent?.phone?.isNotEmpty() == true
        val isOpdSigned = clientModel.opdAgreement?.signedAt?.isNotEmpty() == true
Log.d("Syrgashev", "${this.javaClass.simpleName} hasAgentInfo: $hasAgentInfo")
        return ClientShortViewState(
            firstName = clientModel.firstName,
            lastName = clientModel.lastName,
            phone = clientModel.phone.formatPhoneByMask(phoneMask, "#"),
            hasAgentInfo = hasAgentInfo,
            isOpdSigned = isOpdSigned,
            avatar = userDetails.avatarUrl ?: ""
        )
    }
}