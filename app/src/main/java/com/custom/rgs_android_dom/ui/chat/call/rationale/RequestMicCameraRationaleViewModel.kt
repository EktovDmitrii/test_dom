package com.custom.rgs_android_dom.ui.chat.call.rationale

import com.custom.rgs_android_dom.ui.base.BaseViewModel

class RequestMicCameraRationaleViewModel() : BaseViewModel() {

    fun onSettingsScreenClosed(){
        closeController.value = Unit
    }
}