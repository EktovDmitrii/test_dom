package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class CatalogSubcategoriesViewModel(private val category: CatalogCategoryModel) : BaseViewModel(){

    private val titleController = MutableLiveData<String>()
    val titleObserver: LiveData<String> = titleController

    private val subcategoriesController = MutableLiveData<List<CatalogSubCategoryModel>>()
    val subcategoriesObserver: LiveData<List<CatalogSubCategoryModel>> = subcategoriesController

    init {
        titleController.value = category.title
        subcategoriesController.value = category.subCategories
    }

    fun onBackClick() {
        closeController.value = Unit
    }

}