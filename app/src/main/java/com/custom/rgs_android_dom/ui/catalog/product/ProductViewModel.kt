package com.custom.rgs_android_dom.ui.catalog.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment

class ProductViewModel(
    private val productId: String,
    private val registrationInteractor: RegistrationInteractor,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    private val productServicesController = MutableLiveData<List<ServiceShortModel>>()
    val productServicesObserver: LiveData<List<ServiceShortModel>> = productServicesController

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

        catalogInteractor.getProductServices(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    productServicesController.value = it
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
        if (registrationInteractor.isAuthorized()) {
            productController.value?.let {
                val purchaseServiceModel = PurchaseModel(
                    id = it.id,
                    iconLink = it.iconLink,
                    name = it.name,
                    price = it.price
                )
                val purchaseFragment = PurchaseFragment.newInstance(purchaseServiceModel)
                ScreenManager.showScreenScope(purchaseFragment, PAYMENT)
            }
        }
    }

    fun onServiceClick(serviceShortModel: ServiceShortModel) {
        serviceShortModel.serviceId?.let { id ->
            val serviceFragment = SingleProductFragment.newInstance(id, true)
            ScreenManager.showBottomScreen(serviceFragment)
        }
    }
}
