package com.custom.rgs_android_dom.ui.client.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.OrderItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class OrdersViewModel(
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val ordersController = MutableLiveData<List<OrderItemModel>>()
    val ordersObserver: LiveData<List<OrderItemModel>> = ordersController

    init {
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

    fun onShowCatalogClick() {
    }

    fun onItemClick(itemModel: OrderItemModel) {
    }

    fun onPayClick(itemModel: OrderItemModel) {
    }

    fun onBackClick() {
        closeController.value = Unit
    }

}
