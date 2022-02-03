package com.custom.rgs_android_dom.ui.catalog.tabs.availableservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.AvailableServiceModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabAvailableServicesViewModel(
    private val catalogInteractor: CatalogInteractor,
    private val registrationInteractor: RegistrationInteractor
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
    }

    fun onServiceClick(service: AvailableServiceModel) {
        val singleProductFragment = SingleProductFragment.newInstance(
            SingleProductLauncher(
                productId = service.productId,
                isPurchased = true,
                paidDate = service.validityFrom,
                purchaseObjectId = service.objectIds?.get(0)
            )
        )
        ScreenManager.showBottomScreen(singleProductFragment)
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