package com.custom.rgs_android_dom.ui.client.order_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenInfo
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class OrderDetailViewModel(
    private val chatInteractor: ChatInteractor,
    private val clientInteractor: ClientInteractor,
    private val order: Order
) : BaseViewModel() {

    private val orderViewStateController = MutableLiveData<Order>()
    val orderViewStateObserver: LiveData<Order> = orderViewStateController

    private val showCancelOrderScreenController = MutableLiveData<Order>()
    val showCancelOrderScreenObserver: LiveData<Order> = showCancelOrderScreenController

    private val orderCancelledController = MutableLiveData<Unit>()
    val orderCancelledObserver: LiveData<Unit> = orderCancelledController

    private var orderDetails: Order = order

    init {
        orderViewStateController.value = orderDetails

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

        clientInteractor.getCancelledTasks(order.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    // TODO Replace with enum when we will have all values
                    if (it.isNotEmpty() && it[0].status == "open"){
                        orderCancelledController.value = Unit
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onFeedbackClick() {
        closeController.value = Unit
        ScreenManager.showBottomScreen(ChatFragment.newInstance(
            chatInteractor.getMasterOnlineCase(),
            ScreenInfo(TargetScreen.ORDER_DETAILS, order))
        )
    }

    fun onCancelOrderClick() {
        showCancelOrderScreenController.value = order
    }

    fun onBackClick() {
        closeController.value = Unit
    }
}
