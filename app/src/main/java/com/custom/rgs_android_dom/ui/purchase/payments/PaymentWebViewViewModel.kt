package com.custom.rgs_android_dom.ui.purchase.payments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class PaymentWebViewViewModel(url: String) : BaseViewModel() {

    private val paymentUrlController = MutableLiveData(url)
    val paymentUrlObserver: LiveData<String> = paymentUrlController

    fun onBack() {
        closeController.value = Unit
    }
}