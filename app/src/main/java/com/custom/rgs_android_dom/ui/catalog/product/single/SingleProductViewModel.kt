package com.custom.rgs_android_dom.ui.catalog.product.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SingleProductViewModel(
    private val productId: String,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    private val showMoreDialogController = MutableLiveData<Unit>()
    val showMoreDialogObserver: LiveData<Unit> = showMoreDialogController

    init {
        catalogInteractor.getProduct(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {product->
                    productController.value = product
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onMoreClick(){
        showMoreDialogController.value = Unit
    }

}