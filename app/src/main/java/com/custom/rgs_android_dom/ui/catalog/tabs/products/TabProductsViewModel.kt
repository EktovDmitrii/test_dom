package com.custom.rgs_android_dom.ui.catalog.tabs.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.MyProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabProductsViewModel(
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val myProductsController = MutableLiveData<List<ProductShortModel>>()
    val myProductsObserver: LiveData<List<ProductShortModel>> = myProductsController

    init {
        myProductsController.value = listOf(
            ProductShortModel(
                id = "1",
                type = "",
                title = "Сезонное мытье окон",
                code = "",
                versionId = "",
                name = "",
                price = 0,
                tags = emptyList()
            ),
            ProductShortModel(
                id = "2",
                type = "",
                title = "Сезонное мытье окон",
                code = "",
                versionId = "",
                name = "",
                price = 0,
                tags = emptyList()
            ),
            ProductShortModel(
                id = "3",
                type = "",
                title = "Сезонное мытье окон",
                code = "",
                versionId = "",
                name = "",
                price = 0,
                tags = emptyList()
            ),
            ProductShortModel(
                id = "4",
                type = "",
                title = "Сезонное мытье окон",
                code = "",
                versionId = "",
                name = "",
                price = 0,
                tags = emptyList()
            )
        )
    }

    fun onProductClick(product: ProductShortModel) {
        ScreenManager.showBottomScreen(MyProductFragment.newInstance())
    }
}