package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogPrimaryProductsFragment
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoriesFragment
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabCatalogViewModel(
    private val catalogInteractor: CatalogInteractor,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val catalogCategoriesController = MutableLiveData<List<CatalogCategoryModel>>()
    val catalogCategoriesObserver: LiveData<List<CatalogCategoryModel>> = catalogCategoriesController

    init {
        Observable.merge(registrationInteractor.getLoginSubject().hide(), registrationInteractor.getLogoutSubject().hide())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadCatalogCategories()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        loadCatalogCategories()
    }


    fun onSubCategoryClick(subCategory: CatalogSubCategoryModel){
        if (subCategory.products.isNotEmpty()){
            val catalogSubcategoryFragment = CatalogSubcategoryFragment.newInstance(subCategory)
            ScreenManager.showBottomScreen(catalogSubcategoryFragment)
        }
    }

    fun onAllProductsClick(category: CatalogCategoryModel){
        YandexMetrica.reportEvent("catalog_catalog_service_click_all", "{\"category\":\"${category.name}\"}")

        val catalogSubcategoriesFragment = CatalogSubcategoriesFragment.newInstance(category)
        ScreenManager.showBottomScreen(catalogSubcategoriesFragment)
    }

    fun onAllPrimaryProductsClick(category: CatalogCategoryModel){
        val primSubcategoriesFragment = CatalogPrimaryProductsFragment.newInstance(category)
        ScreenManager.showBottomScreen(primSubcategoriesFragment)
    }

    fun onProductClick(productModel: ProductShortModel) {
        if (productModel.defaultProduct){
            ScreenManager.showBottomScreen(SingleProductFragment.newInstance(SingleProductLauncher(productModel.id, productModel.versionId)))
        } else {
            ScreenManager.showBottomScreen(ProductFragment.newInstance(ProductLauncher(productModel.id, productModel.versionId)))
        }
    }

    private fun loadCatalogCategories(){
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
}
