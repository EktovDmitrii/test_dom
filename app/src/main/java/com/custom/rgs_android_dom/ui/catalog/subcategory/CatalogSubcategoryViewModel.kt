package com.custom.rgs_android_dom.ui.catalog.subcategory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class CatalogSubcategoryViewModel(private val subCategory: CatalogSubCategoryModel) : BaseViewModel(){

    private val titleController = MutableLiveData<String>()
    val titleObserver: LiveData<String> = titleController

    private val iconController = MutableLiveData<String>()
    val iconObserver: LiveData<String> = iconController

    private val productsController = MutableLiveData<List<ProductShortModel>>()
    val productsObserver: LiveData<List<ProductShortModel>> = productsController

    init {
        titleController.value = subCategory.title
        productsController.value = subCategory.products
        iconController.value = subCategory.icon
    }

    fun onBackClick() {
        closeController.value = Unit
    }

}