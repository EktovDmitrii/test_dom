package com.custom.rgs_android_dom.ui.catalog.tabs.availableservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.AvailableServiceModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceFragment
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceLauncher
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabAvailableServicesViewModel(
    private val catalogInteractor: CatalogInteractor,
    private val registrationInteractor: RegistrationInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val availableServicesController = MutableLiveData<List<AvailableServiceModel>>()
    val availableServicesObserver: LiveData<List<AvailableServiceModel>> = availableServicesController

    private val isNoServicesLayoutVisibleController = MutableLiveData<Unit>()
    val isNoServicesLayoutVisibleObserver: LiveData<Unit> = isNoServicesLayoutVisibleController

    init {
        loadAvailableServices()

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadAvailableServices()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadAvailableServices()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        purchaseInteractor.getProductPurchasedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadAvailableServices()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        purchaseInteractor.getServiceOrderedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadAvailableServices()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onServiceClick(service: AvailableServiceModel) {
        catalogInteractor.getProduct(service.productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { product ->
                    YandexMetrica.reportEvent("catalog_available_service", "{\"my_products\":\"${product.name}\",\"available_service\":\"${service.serviceName}\"}")

                    if (product.defaultProduct) {
                        ScreenManager.showBottomScreen(
                            SingleProductFragment.newInstance(
                                SingleProductLauncher(
                                    productId = product.id,
                                    isPurchased = true
                                )
                            )
                        )
                    } else {
                        ScreenManager.showBottomScreen(
                            ServiceFragment.newInstance(
                                ServiceLauncher(
                                    productId = service.productId,
                                    serviceId = service.serviceId,
                                    isPurchased = true,
                                    purchaseObjectId = service.objectId,
                                    purchaseValidFrom = service.validityFrom,
                                    purchaseValidTo = service.validityTo,
                                    quantity = service.available.toLong(),
                                    canBeOrdered = service.canBeOrdered
                                )
                            )
                        )
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun loadAvailableServices(){
        if (registrationInteractor.isAuthorized()){
            catalogInteractor.getAvailableServices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        availableServicesController.value = it
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        } else {
            isNoServicesLayoutVisibleController.value = Unit
        }
    }
}