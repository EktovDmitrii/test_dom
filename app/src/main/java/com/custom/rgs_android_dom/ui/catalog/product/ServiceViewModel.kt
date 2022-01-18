package com.custom.rgs_android_dom.ui.catalog.product

import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class ServiceViewModel(private val product: ProductModel) : BaseViewModel() {

    fun onBackClick(){
        closeController.value = Unit
    }
}