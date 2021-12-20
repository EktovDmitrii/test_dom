package com.custom.rgs_android_dom.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedNavViewModel : ViewModel() {

    private val isNavigationVisibleController = MutableLiveData(true)
    val isNavigationVisibleObserver: LiveData<Boolean> = isNavigationVisibleController

    fun hideNav() {
        isNavigationVisibleController.value = false
    }

    fun showNav() {
        isNavigationVisibleController.value = true
    }

}