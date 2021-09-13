package com.custom.rgs_android_dom.ui.chat

import com.custom.rgs_android_dom.ui.base.BaseViewModel

class ChatViewModel() : BaseViewModel() {



    init {

    }

    fun onBackClick(){
        closeController.value = Unit
    }

}