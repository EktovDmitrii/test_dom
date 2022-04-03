package com.custom.rgs_android_dom.ui.purchase.add.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class AddEmailViewModel(private val email: String) : BaseViewModel() {

    private val emailController = MutableLiveData<String>()
    val emailObserver: LiveData<String> = emailController

    init {
        emailController.value = email
    }

}