package com.custom.rgs_android_dom.ui.client.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class OrderDetailViewModel(
    private val clientInteractor: ClientInteractor,
    private val order: Order
) : BaseViewModel() {

    private val orderViewStateController = MutableLiveData<Order>()
    val orderViewStateObserver: LiveData<Order> = orderViewStateController

    private var orderDetails: Order = order

    init {
        orderViewStateController.value = orderDetails
    }

    fun onFeedbackClick() {
        ScreenManager.showBottomScreen(ChatFragment())
    }

    fun onCancelOrderClick() {
        orderDetails = orderDetails.copy(status = OrderStatus.CANCELLED)
        orderViewStateController.value = orderDetails
    }

    fun onBackClick() {
        closeController.value = Unit
    }
}
