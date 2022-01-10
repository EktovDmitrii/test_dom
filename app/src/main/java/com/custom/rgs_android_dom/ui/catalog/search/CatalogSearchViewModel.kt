package com.custom.rgs_android_dom.ui.catalog.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CatalogSearchViewModel(
    private val tag: String?,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    companion object {
        private const val SEARCH_DELAY = 500L
    }

    private val productsController = MutableLiveData<List<ProductShortModel>>()
    val productsObserver: LiveData<List<ProductShortModel>> = productsController

    private val searchSubject = PublishRelay.create<String>()
    private var products: List<ProductShortModel>? = null

    init {
        catalogInteractor.getProductsAvailableForPurchase(tag)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    products = it
                    productsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        subscribeSearchSubject()
    }


    fun onSearchQueryChanged(query: String){
        searchSubject.accept(query)
    }

    fun onClearClick(){
        products?.let{
            productsController.value = it
        }
    }

    fun onProductClick(product: ProductShortModel){

    }

    private fun subscribeSearchSubject() {
        searchSubject.hide()
            .debounce(SEARCH_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .flatMapSingle {
                catalogInteractor.getProductsAvailableForPurchase(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    productsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}