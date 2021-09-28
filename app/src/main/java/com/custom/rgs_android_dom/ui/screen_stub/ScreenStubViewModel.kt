package com.custom.rgs_android_dom.ui.screen_stub

import com.custom.rgs_android_dom.ui.base.BaseViewModel

class ScreenStubViewModel() : BaseViewModel() {


    init {

    }

    fun onBackClick(){
        closeController.value = Unit
    }

}