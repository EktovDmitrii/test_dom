package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.util.Log
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class CatalogSubcategoriesViewModel(private val category: CatalogCategoryModel) : BaseViewModel(){

    init {
        Log.d("MyLog", "CATEGORY " + category.title)
    }

}