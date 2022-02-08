package com.custom.rgs_android_dom.ui.catalog.product.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderFragment
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ServiceViewModel(
    private val service: ServiceLauncher,
    private val catalogInteractor: CatalogInteractor,
    private val propertyInteractor: PropertyInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val serviceController = MutableLiveData<ServiceModel>()
    val serviceObserver: LiveData<ServiceModel> = serviceController

    private val productAddressController = MutableLiveData<String?>()
    val productAddressObserver: LiveData<String?> = productAddressController

    private val productValidToController = MutableLiveData<String?>()
    val productValidToObserver: LiveData<String?> = productValidToController

    private val productPaidDateController = MutableLiveData<String?>()
    val productPaidDateObserver: LiveData<String?> = productPaidDateController

    private val orderTextViewVisibleController = MutableLiveData<Boolean>()
    val orderTextViewVisibleObserver: LiveData<Boolean> = orderTextViewVisibleController

    private val priceTextViewVisibleController = MutableLiveData<Boolean>()
    val priceTextViewVisibleObserver: LiveData<Boolean> = orderTextViewVisibleController

    init {
        productValidToController.value = service.purchaseValidTo?.formatTo(
            DATE_PATTERN_DATE_FULL_MONTH
        )
        productPaidDateController.value = service.paidDate?.formatTo(DATE_PATTERN_DATE_ONLY)

        catalogInteractor.getProductServiceDetails(service.productId, service.serviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    serviceController.value = it
                    orderTextViewVisibleController.value = service.isPurchased && service.quantity > 0
                    priceTextViewVisibleController.value = service.isPurchased
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        service.purchaseObjectId?.let { objectId ->
            propertyInteractor.getPropertyItem(objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        productAddressController.value = it.address?.address
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }

        purchaseInteractor.getServiceOrderedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    checkServiceAvailability()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onServiceOrderClick(){
        val serviceOrderFragment = ServiceOrderFragment.newInstance(service.serviceId, service.productId)
        ScreenManager.showScreen(serviceOrderFragment)
    }

    private fun checkServiceAvailability(){
        catalogInteractor.getAvailableServiceInProduct(service.productId, service.serviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    orderTextViewVisibleController.value = service.isPurchased &&  it.available > 0
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}