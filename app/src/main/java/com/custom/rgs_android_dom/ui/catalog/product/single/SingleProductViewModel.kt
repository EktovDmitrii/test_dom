package com.custom.rgs_android_dom.ui.catalog.product.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class SingleProductViewModel(private val product: ProductModel) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    init {
        productController.value = product
    }

    fun onBackClick(){
        closeController.value = Unit
    }

}