package com.custom.rgs_android_dom.ui.update_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class UpdateAppViewModel() : BaseViewModel() {

    private val startUpdateController = MutableLiveData<Unit>()
    val startUpdateObserver: LiveData<Unit> = startUpdateController

    fun onUpdateClick() {
        startUpdateController.value = Unit
    }

    fun onNotNowClick() {
        close()
    }


}