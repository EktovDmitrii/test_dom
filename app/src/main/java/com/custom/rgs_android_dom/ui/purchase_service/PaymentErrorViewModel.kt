package com.custom.rgs_android_dom.ui.purchase_service

import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class PaymentErrorViewModel(private val firstFragmentId: Int) : BaseViewModel() {

    fun onCloseScope() {
        closeController.value = Unit
    }

    fun navigatePurchase() {
        ScreenManager.closeScreenToId(PAYMENT, firstFragmentId)
    }
}