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

    private val tagController = MutableLiveData<String>()
    val tagObserver: LiveData<String> = tagController

    private val arePopularProductVisibleController = MutableLiveData<Boolean>()
    val arePopularProductsVisibleObserver: LiveData<Boolean> = arePopularProductVisibleController

    private val areSearchResultsVisibleController = MutableLiveData<Boolean>()
    val areSearchResultsVisibleObserver: LiveData<Boolean> = areSearchResultsVisibleController

    private val popularProductsController = MutableLiveData<List<ProductShortModel>>()
    val popularProductsObserver: LiveData<List<ProductShortModel>> = popularProductsController

    private val searchResultsController = MutableLiveData<List<ProductShortModel>>()
    val searchResultsObserver: LiveData<List<ProductShortModel>> = searchResultsController

    private val searchSubject = PublishRelay.create<String>()

    init {
        tag?.let { tag->
            tagController.value = tag
        }

        arePopularProductVisibleController.value = (tag == null)

        catalogInteractor.getPopularProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    popularProductsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        subscribeSearchSubject()
    }


    fun onSearchQueryChanged(query: String){
        if (query.isNotEmpty()){
            arePopularProductVisibleController.value = false
            searchSubject.accept(query)
        } else {
            arePopularProductVisibleController.value = true
            areSearchResultsVisibleController.value = false
        }
    }

    fun onClearClick(){
        arePopularProductVisibleController.value = true
        areSearchResultsVisibleController.value = false
    }

    fun onProductClick(product: ProductShortModel){

    }

    private fun subscribeSearchSubject() {
        searchSubject.hide()
            .debounce(SEARCH_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapSingle {
                catalogInteractor.getProductsAvailableForPurchase(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()){
                        searchResultsController.value = it
                    }
                    areSearchResultsVisibleController.value = it.isNotEmpty()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}