package com.custom.rgs_android_dom.ui.main.stub

import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment

class MainStubViewModel(private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    fun onStubClick(){
        if (registrationInteractor.isAuthorized()){
            ScreenManager.showBottomScreen(ClientFragment())
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

}