package com.custom.rgs_android_dom.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class MainCatalogViewModel(
    val tab: Int
) : BaseViewModel() {

    private val tabController = MutableLiveData<Int>()
    val tabObserver: LiveData<Int> = tabController

    init {
        tabController.value = tab
    }

    fun onSearchClick() {
        val catalogSearchFragment = CatalogSearchFragment.newInstance()
        ScreenManager.showScreen(catalogSearchFragment)
    }

}