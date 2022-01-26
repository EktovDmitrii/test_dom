package com.custom.rgs_android_dom.ui.policies.add

import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantFragment

class AddPolicyViewModel : BaseViewModel() {

    fun onBackClick() {
        close()
    }

    fun onNextClick() {
        ScreenManager.showScreen(InsurantFragment())
    }

}