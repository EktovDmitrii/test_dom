package com.custom.rgs_android_dom.ui.client.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class OrderDetailViewModel(
    private val chatInteractor: ChatInteractor,
    private val order: Order
) : BaseViewModel() {

    private val orderViewStateController = MutableLiveData<Order>()
    val orderViewStateObserver: LiveData<Order> = orderViewStateController

    private var orderDetails: Order = order

    init {
        orderViewStateController.value = orderDetails
    }

    fun onFeedbackClick() {
        closeController.value = Unit
        ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
    }

    fun onCancelOrderClick() {
        orderDetails = orderDetails.copy(status = OrderStatus.CANCELLED)
        orderViewStateController.value = orderDetails
    }

    fun onBackClick() {
        closeController.value = Unit
    }
}
