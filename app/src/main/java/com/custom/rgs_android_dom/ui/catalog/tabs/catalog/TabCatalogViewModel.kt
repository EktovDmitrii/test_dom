package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.*
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabCatalogViewModel(private val catalogInteractor: CatalogInteractor) : BaseViewModel() {

    private val viewStateController = MutableLiveData<ProductViewState>()
    val viewStateObserver: LiveData<ProductViewState> = viewStateController

    private val catalogCategoriesController = MutableLiveData<List<CatalogCategoryModel>>()
    val catalogCategoriesObserver: LiveData<List<CatalogCategoryModel>> = catalogCategoriesController

    init {
        catalogInteractor.getCatalogCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.value = LoadingState.LOADING
            }
            .subscribeBy(
                onSuccess = {
                    catalogCategoriesController.value = it
                    loadingStateController.value = LoadingState.CONTENT
                },
                onError = {
                    logException(this, it)
                    loadingStateController.value = LoadingState.CONTENT
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onShowAllPrimaryCategoryClick() {
        notificationController.value = "onShowAllPrimaryCategoryClick"
    }

    fun onProductClick() {
        //navigate to product or smth
        notificationController.value = "onProductClick"
    }

    fun onMoreProductsClick() {
        // retrieve more products
        notificationController.value = "onMoreProductsClick"
    }

    fun onServiceClick() {
        notificationController.value = "onServiceClick"
    }

}
