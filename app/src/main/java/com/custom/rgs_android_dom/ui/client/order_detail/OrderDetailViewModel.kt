package com.custom.rgs_android_dom.ui.client.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.view_states.OrderDetailViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class OrderDetailViewModel(
    private val clientInteractor: ClientInteractor,
    private val order: Order
) : BaseViewModel() {

    private val orderViewStateController = MutableLiveData<OrderDetailViewState>()
    val orderViewStateObserver: LiveData<OrderDetailViewState> = orderViewStateController

    fun onFeedbackClick() {
    }

    fun onCancelOrderClick() {
    }

    fun onBackClick() {
        closeController.value = Unit
    }
}
