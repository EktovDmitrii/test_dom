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
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderLauncher
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
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

    private val productValidityFromToController = MutableLiveData<Pair<String, String>?>()
    val productValidityFromToObserver: LiveData<Pair<String, String>?> = productValidityFromToController

    private val orderTextViewVisibleController = MutableLiveData<Boolean>()
    val orderTextViewVisibleObserver: LiveData<Boolean> = orderTextViewVisibleController

    private val priceTextViewVisibleController = MutableLiveData<Boolean>()
    val priceTextViewVisibleObserver: LiveData<Boolean> = priceTextViewVisibleController

    private val isOrderTextViewEnabledController = MutableLiveData<Boolean>()
    val isOrderTextViewEnabledObserver: LiveData<Boolean> = isOrderTextViewEnabledController

    init {
        setValidityDate()

        isOrderTextViewEnabledController.value = service.canBeOrdered

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

    private fun setValidityDate() {
        productValidityFromToController.value = if (service.purchaseValidFrom != null && service.purchaseValidFrom.isAfterNow){
            "Действует с" to service.purchaseValidFrom.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        } else if (service.purchaseValidTo != null && service.purchaseValidTo.isAfterNow){
            "Действует до" to service.purchaseValidTo.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        } else null
    }

    fun onServiceOrderClick(){
        val serviceOrderFragment = ServiceOrderFragment.newInstance(
            ServiceOrderLauncher(service.serviceId, service.productId)
        )
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