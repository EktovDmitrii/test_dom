package com.custom.rgs_android_dom.ui.client.agent.edit

import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class EditAgentViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {


    init {

    }

    fun onBackClick(){
        closeController.value = Unit
    }




}