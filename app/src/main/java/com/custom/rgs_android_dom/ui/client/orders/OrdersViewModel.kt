package com.custom.rgs_android_dom.ui.client.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderItemModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.client.order_detail.OrderDetailFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class OrdersViewModel(
    private val clientInteractor: ClientInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val ordersController = MutableLiveData<List<Order>>()
    val ordersObserver: LiveData<List<Order>> = ordersController

    init {
        loadOrderHistory()
        purchaseInteractor.getProductPurchasedSubject()
            .mergeWith(purchaseInteractor.getServiceOrderedSubject())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadOrderHistory()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onReloadClick() = loadOrderHistory()

    fun onShowCatalogClick() {
        closeController.value = Unit
        ScreenManager.showBottomScreen(MainCatalogFragment.newInstance())
    }

    fun onItemClick(order: Order) {
        ScreenManager.showScreen(OrderDetailFragment.newInstance(order))
    }

    fun onPayClick(purchaseModel: PurchaseModel) {
        val purchaseFragment = PurchaseFragment.newInstance(purchaseModel)
        ScreenManager.showScreenScope(purchaseFragment, PAYMENT)
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    private fun loadOrderHistory() {
        clientInteractor.getOrdersHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    ordersController.value = it
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)
    }

}
