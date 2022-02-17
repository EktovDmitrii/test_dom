package com.custom.rgs_android_dom.ui.catalog.product.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SingleProductViewModel(
    private val product: SingleProductLauncher,
    private val registrationInteractor: RegistrationInteractor,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    init {
        catalogInteractor.getProduct(product.productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { product ->
                    productController.value = product.copy(
                        isPurchased = this.product.isPurchased
                    )
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
        productController.value?.let { product ->
            if (registrationInteractor.isAuthorized()) {
                if (product.isPurchased){
                    product.validityFrom?.let { validityFrom->
                        catalogInteractor.getProductServices(product.id, product.isPurchased, product.validityFrom)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onSuccess = {
                                    if (it.isNotEmpty()){
                                        val serviceOrderFragment = ServiceOrderFragment.newInstance(
                                            it[0].serviceId,
                                            product.id
                                        )
                                        ScreenManager.showScreen(serviceOrderFragment)
                                    }

                                },
                                onError = {
                                    logException(this, it)
                                }
                            ).addTo(dataCompositeDisposable)
                    }
                } else {
                    val purchaseServiceModel = PurchaseModel(
                        id = product.id,
                        defaultProduct = product.defaultProduct,
                        duration = product.duration,
                        deliveryTime = product.deliveryTime,
                        //deliveryType = product.deliveryType,
                        logoSmall = product.logoSmall,
                        name = product.name,
                        price = product.price
                    )
                    val purchaseFragment = PurchaseFragment.newInstance(purchaseServiceModel)
                    ScreenManager.showScreenScope(purchaseFragment, PAYMENT)
                }
            } else {
                ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
            }
        }
    }

}