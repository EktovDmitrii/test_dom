package com.custom.rgs_android_dom.ui.catalog.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductDurationModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductPriceModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductUnitType
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseServiceModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase_service.PurchaseFragment

class ProductViewModel(
    private val productId: String,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    init {
        catalogInteractor.getProduct(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { product ->
                    productController.value = product
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onCheckoutClick() {
        productController.value?.let {
            val purchaseServiceModel = PurchaseServiceModel(
                id = it.id,
                iconLink = it.iconLink,
                name = it.name,
                price = it.price
            )
            val purchaseServiceFragment = PurchaseFragment.newInstance(purchaseServiceModel)
            ScreenManager.showBottomScreen(purchaseServiceFragment)
        }

    }

    fun onServiceClick() {
        val product = ProductModel(
            code = "",
            activatedAt = null,
            archivedAt = null,
            coolOff = null,
            createdAt = null,
            defaultProduct = false,
            deliveryTime = null,
            description = "",
            descriptionFormat = "",
            descriptionRef = null,
            duration = ProductDurationModel(
                units = 12,
                unitType = ProductUnitType.DAYS
            ),
            iconLink = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
            id = "1",
            insuranceProducts = null,
            internalDescription = null,
            name = "",
            objectRequired = null,
            price = ProductPriceModel(amount = 1500, vatType = null),
            status = null,
            tags = emptyList(),
            title = "Поиск утечек тепла, сквозняков.",
            type = "",
            validityFrom = null,
            validityTo = null,
            versionActivatedAt = null,
            versionArchivedAt = null,
            versionCode = null,
            versionCreatedAt = null,
            versionId = null,
            versionStatus = null
        )
        val serviceFragment = ServiceFragment.newInstance(product)
        ScreenManager.showBottomScreen(serviceFragment)
    }
}
