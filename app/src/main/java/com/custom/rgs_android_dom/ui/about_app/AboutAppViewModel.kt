package com.custom.rgs_android_dom.ui.about_app

import com.custom.rgs_android_dom.ui.base.BaseViewModel

class AboutAppViewModel() : BaseViewModel() {


    init {

    }

    fun onBackClick(){
        closeController.value = Unit
    }

}