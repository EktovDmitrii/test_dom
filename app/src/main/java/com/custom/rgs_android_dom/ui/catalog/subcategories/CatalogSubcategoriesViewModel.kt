package com.custom.rgs_android_dom.ui.catalog.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryFragment

class CatalogSubcategoriesViewModel(
    private val category: CatalogCategoryModel,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel(){

    private val titleController = MutableLiveData<String>()
    val titleObserver: LiveData<String> = titleController

    private val subcategoriesController = MutableLiveData<List<CatalogSubCategoryModel>>()
    val subcategoriesObserver: LiveData<List<CatalogSubCategoryModel>> = subcategoriesController

    private val productsController = MutableLiveData<List<ProductShortModel>>()
    val productsObserver: LiveData<List<ProductShortModel>> = productsController

    init {
        titleController.value = category.title
        subcategoriesController.value = category.subCategories
        productsController.value = category.products
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onProductClick(product: ProductShortModel){
        // TODO Replace this, when we will have guest endpoint for product details
        if (registrationInteractor.isAuthorized()){
            if (product.defaultProduct){
                // Open service (single product) details screen
                ScreenManager.showBottomScreen(SingleProductFragment.newInstance(product.id))
            } else {
                // Open product details screen
                ScreenManager.showBottomScreen(ProductFragment.newInstance(product.id))
            }
        }
    }

    fun onSubCategoryClick(subCategory: CatalogSubCategoryModel) {
        if (subCategory.products.isNotEmpty()){
            val catalogSubcategoryFragment = CatalogSubcategoryFragment.newInstance(subCategory)
            ScreenManager.showBottomScreen(catalogSubcategoryFragment)
        }
    }

}