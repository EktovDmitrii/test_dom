package com.custom.rgs_android_dom.ui.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class DemoViewModel: BaseViewModel() {

    private val demoTextController = MutableLiveData<String>()
    val demoTextObserver : LiveData<String> = demoTextController


    init {
        loadData()
    }

    private fun loadData(){

    }
}