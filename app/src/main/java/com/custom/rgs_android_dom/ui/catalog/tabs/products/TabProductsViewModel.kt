package com.custom.rgs_android_dom.ui.catalog.tabs.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.MyProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class TabProductsViewModel(
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val myProductsController = MutableLiveData<List<ProductShortModel>>()
    val myProductsObserver: LiveData<List<ProductShortModel>> = myProductsController

    fun onProductClick(product: ProductShortModel) {
        ScreenManager.showBottomScreen(MyProductFragment.newInstance())
    }
}