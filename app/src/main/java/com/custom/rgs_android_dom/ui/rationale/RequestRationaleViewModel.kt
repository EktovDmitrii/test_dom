package com.custom.rgs_android_dom.ui.rationale

import com.custom.rgs_android_dom.ui.base.BaseViewModel

class RequestRationaleViewModel() : BaseViewModel() {

    fun onSettingsScreenClosed(){
        closeController.value = Unit
    }
}