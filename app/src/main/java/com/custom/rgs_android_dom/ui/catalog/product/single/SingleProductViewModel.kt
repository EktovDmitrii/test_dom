package com.custom.rgs_android_dom.ui.catalog.product.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderLauncher
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
        catalogInteractor.getProductByVersion(product.productId, product.productVersionId)
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
                catalogInteractor.getProductServicesByVersion(
                    product.id,
                    product.versionId,
                    product.isPurchased,
                    product.validityFrom,
                    null
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = {
                            if (it.isNotEmpty()) {
                                if (product.isPurchased) { // TODO временно до новой другой задачи
                                    val serviceOrderFragment = ServiceOrderFragment.newInstance(
                                        ServiceOrderLauncher(
                                            serviceId = it[0].serviceId,
                                            productId = product.id,
                                            serviceVersionId = it[0].serviceVersionId,
                                            deliveryType = it[0].serviceDeliveryType,
                                            clientProductId = ""
                                        )
                                    )
                                    ScreenManager.showScreen(serviceOrderFragment)
                                } else {
                                    val purchaseServiceModel = PurchaseModel(
                                        id = product.id,
                                        versionId = product.versionId ?: "",
                                        defaultProduct = product.defaultProduct,
                                        duration = product.duration,
                                        deliveryTime = product.deliveryTime,
                                        deliveryType = it[0].serviceDeliveryType,
                                        logoSmall = product.logoSmall,
                                        name = product.name,
                                        price = product.price
                                    )
                                    val purchaseFragment = PurchaseFragment.newInstance(purchaseServiceModel)
                                    //ScreenManager.showScreenScope(purchaseFragment, PAYMENT)
                                    ScreenManager.showBottomScreen(purchaseFragment)
                                }
                            }
                        },
                        onError = {
                            logException(this, it)
                        }
                    ).addTo(dataCompositeDisposable)
            } else {
                ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
            }
        }
    }

}