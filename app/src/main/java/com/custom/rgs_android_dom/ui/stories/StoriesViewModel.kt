package com.custom.rgs_android_dom.ui.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class StoriesViewModel(tab: Int) : BaseViewModel() {

    private val tabController = MutableLiveData<Int>()
    val tabObserver: LiveData<Int> = tabController

    init {
        tabController.value = tab
    }

    fun onBackClick() {
        close()
    }

    fun onUnderstandClick() {
        close()
    }

    fun setTab(tab: Int) {
        tabController.value = tab
    }

}