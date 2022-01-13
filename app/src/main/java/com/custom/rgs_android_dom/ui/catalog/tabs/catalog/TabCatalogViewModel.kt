package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogPrimaryProductsFragment
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoriesFragment
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabCatalogViewModel(private val catalogInteractor: CatalogInteractor) : BaseViewModel() {


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


    fun onSubCategoryClick(subCategory: CatalogSubCategoryModel){
        if (subCategory.products.isNotEmpty()){
            val catalogSubcategoryFragment = CatalogSubcategoryFragment.newInstance(subCategory)
            ScreenManager.showBottomScreen(catalogSubcategoryFragment)
        }
    }

    fun onAllProductsClick(category: CatalogCategoryModel){
        val catalogSubcategoriesFragment = CatalogSubcategoriesFragment.newInstance(category)
        ScreenManager.showBottomScreen(catalogSubcategoriesFragment)
    }

    fun onAllPrimaryProductsClick(category: CatalogCategoryModel){
        val primSubcategoriesFragment = CatalogPrimaryProductsFragment.newInstance(category)
        ScreenManager.showBottomScreen(primSubcategoriesFragment)
    }

    fun onProductClick(productModel: ProductShortModel) {
        ScreenManager.showBottomScreen(ProductFragment.newInstance(productModel.id))
    }
}
