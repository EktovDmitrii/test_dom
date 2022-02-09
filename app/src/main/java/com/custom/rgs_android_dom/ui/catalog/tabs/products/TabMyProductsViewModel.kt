package com.custom.rgs_android_dom.ui.catalog.tabs.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ClientProductModel
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.policies.PoliciesFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TabMyProductsViewModel(
    private val catalogInteractor: CatalogInteractor,
    private val registrationInteractor: RegistrationInteractor,
    private val purchaseInteractor: PurchaseInteractor,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val myProductsController = MutableLiveData<List<ClientProductModel>>()
    val myProductsObserver: LiveData<List<ClientProductModel>> = myProductsController

    private val isNoProductsLayoutVisibleController = MutableLiveData<Unit>()
    val isNoProductsLayoutVisibleObserver: LiveData<Unit> = isNoProductsLayoutVisibleController

    private var requestedScreen = TargetScreen.UNSPECIFIED

    init {
        loadClientProducts()

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadClientProducts()
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
                    loadClientProducts()
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
                    loadClientProducts()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getClientSavedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    if (registrationInteractor.isAuthorized()) {
                        when (requestedScreen) {
                            TargetScreen.POLICIES -> ScreenManager.showScreen(PoliciesFragment())
                            else -> {}
                        }
                        requestedScreen = TargetScreen.UNSPECIFIED
                    }
                },
                onError = { logException(this, it) }
            ).addTo(dataCompositeDisposable)
    }

    fun onProductClick(product: ClientProductModel) {
        if (product.defaultProduct){
            ScreenManager.showBottomScreen(
                SingleProductFragment.newInstance(
                    SingleProductLauncher(
                        productId = product.productId,
                        isPurchased = true
                    )
                )
            )
        } else {
            ScreenManager.showBottomScreen(
                ProductFragment.newInstance(
                    ProductLauncher(
                        productId = product.productId,
                        isPurchased = true,
                        purchaseValidTo = product.validityTo,
                        purchaseObjectId = product.objectId
                    )
                )
            )
        }
    }

    fun onAddPolicyClick(){
        requestedScreen = TargetScreen.POLICIES
        if (registrationInteractor.isAuthorized()) {
            ScreenManager.showScreen(PoliciesFragment())
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    private fun loadClientProducts(){
        if (registrationInteractor.isAuthorized()){
            catalogInteractor.getClientProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        myProductsController.value = it
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        } else {
            isNoProductsLayoutVisibleController.value = Unit
        }

    }

}