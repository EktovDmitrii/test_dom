package com.custom.rgs_android_dom.ui.location.rationale

import com.custom.rgs_android_dom.ui.base.BaseViewModel

class RequestLocationRationaleViewModel() : BaseViewModel() {

    fun onSettingsScreenClosed(){
        closeController.value = Unit
    }
}