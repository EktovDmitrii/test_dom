package com.custom.rgs_android_dom.ui.registration.code

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class RegistrationCodeViewModel(val phone: String) : BaseViewModel() {

    private val isErrorVisibleController = MutableLiveData<Boolean>()
    private val phoneController = MutableLiveData<String>()

    val isErrorVisibleObserver: LiveData<Boolean> = isErrorVisibleController
    val phoneObserver: LiveData<String> = phoneController

    init {
        phoneController.value = phone
    }

    fun onCloseClick(){

    }

    fun onCodeComplete(code: String){
        Log.d("MyLog", "On code complete: " + code)
        isErrorVisibleController.value = false
    }


}