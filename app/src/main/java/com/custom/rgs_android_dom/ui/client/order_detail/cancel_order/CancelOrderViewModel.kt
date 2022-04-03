package com.custom.rgs_android_dom.ui.client.order_detail.cancel_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class CancelOrderViewModel(
    private val order: Order,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val orderCancelledController = MutableLiveData<Unit>()
    val orderCancelledObserver: LiveData<Unit> = orderCancelledController

    init {
        clientInteractor.getOrderCancelledSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    orderCancelledController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCancelOrderClick(){
        clientInteractor.cancelOrder(order)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.value = LoadingState.LOADING
            }
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}
