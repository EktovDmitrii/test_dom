package com.custom.rgs_android_dom.ui.catalog.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class CatalogPrimarySubcategoryViewModel(private val category: CatalogCategoryModel) : BaseViewModel() {

    private val subcategoriesController = MutableLiveData<List<CatalogSubCategoryModel>>()
    val subcategoriesObserver: LiveData<List<CatalogSubCategoryModel>> = subcategoriesController

    init {
        subcategoriesController.value = category.subCategories
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onSubCategoryClick(subCategoryModel: CatalogSubCategoryModel) {
        if (subCategoryModel.products.isNotEmpty()){
            val catalogSubcategoryFragment = CatalogSubcategoryFragment.newInstance(subCategoryModel)
            ScreenManager.showBottomScreen(catalogSubcategoryFragment)
        }
    }

}
