package com.custom.rgs_android_dom.ui.catalog.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchFragment

class CatalogSubcategoryViewModel(
    private val subCategory: CatalogSubCategoryModel,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val titleController = MutableLiveData<String>()
    val titleObserver: LiveData<String> = titleController

    private val iconController = MutableLiveData<String>()
    val iconObserver: LiveData<String> = iconController

    private val productsController = MutableLiveData<List<ProductShortModel>>()
    val productsObserver: LiveData<List<ProductShortModel>> = productsController

    init {
        titleController.value = subCategory.name
        productsController.value = subCategory.products
        iconController.value = subCategory.logoMiddle
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onSearchClick() {
        val catalogSearchFragment = CatalogSearchFragment.newInstance()
        ScreenManager.showScreen(catalogSearchFragment)
    }

    fun onProductClick(product: ProductShortModel) {
        if (product.defaultProduct) {
            // Open service (single product) details screen
            ScreenManager.showBottomScreen(SingleProductFragment.newInstance(product.id))
        } else {
            // Open product details screen
            ScreenManager.showBottomScreen(ProductFragment.newInstance(product.id))
        }
    }

}