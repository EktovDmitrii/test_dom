package com.custom.rgs_android_dom.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class MainCatalogViewModel(
    val tab: String
) : BaseViewModel() {

    private val tabController = MutableLiveData<String>()
    val tabObserver: LiveData<String> = tabController

    init {
        tabController.value = tab
    }

}