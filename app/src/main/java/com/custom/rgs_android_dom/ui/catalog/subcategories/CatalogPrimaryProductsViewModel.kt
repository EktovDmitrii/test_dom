package com.custom.rgs_android_dom.ui.catalog.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class CatalogPrimaryProductsViewModel(private val category: CatalogCategoryModel) :
    BaseViewModel() {

    private val productsController = MutableLiveData<List<ProductShortModel>>()
    val productsObserver: LiveData<List<ProductShortModel>> = productsController

    private val titleController = MutableLiveData<String>()
    val titleObserver: LiveData<String> = titleController

    init {
        productsController.value = category.products
        titleController.value = category.name
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onSearchClick() {
        val catalogSearchFragment = CatalogSearchFragment.newInstance()
        ScreenManager.showScreen(catalogSearchFragment)
    }

    fun onProductClick(productModel: ProductShortModel) {
        if (productModel.defaultProduct){
            ScreenManager.showBottomScreen(SingleProductFragment.newInstance(SingleProductLauncher(productModel.id, productModel.versionId)))
        } else {
            ScreenManager.showBottomScreen(ProductFragment.newInstance(ProductLauncher(productModel.id, productModel.versionId)))
        }
    }

}
