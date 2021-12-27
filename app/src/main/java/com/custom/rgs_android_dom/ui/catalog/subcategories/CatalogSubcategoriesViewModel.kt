package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CatalogSubcategoriesViewModel(
    private val category: CatalogCategoryModel,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel(){

    private val titleController = MutableLiveData<String>()
    val titleObserver: LiveData<String> = titleController

    private val subcategoriesController = MutableLiveData<List<CatalogSubCategoryModel>>()
    val subcategoriesObserver: LiveData<List<CatalogSubCategoryModel>> = subcategoriesController

    init {
        titleController.value = category.title
        subcategoriesController.value = category.subCategories
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onProductClick(product: ProductShortModel){
        catalogInteractor.getProduct(product.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {product->

                    if (!product.defaultProduct){
                        // Open service (single product) details screen
                        val singleProductFragment = SingleProductFragment.newInstance(product)
                        ScreenManager.showBottomScreen(singleProductFragment)
                    } else {
                        // Open product details screen
                    }

                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}