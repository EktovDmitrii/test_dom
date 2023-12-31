package com.custom.rgs_android_dom.ui.purchase.payments.error

import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class PaymentErrorViewModel(
    private val chatInteractor: ChatInteractor
    ) : BaseViewModel() {

    fun onCloseScope() {
        closeController.value = Unit
    }

    fun navigatePurchase() {
        ScreenManager.closeScope(PAYMENT)
    }

    fun navigateChat() {
        ScreenManager.closeScope(PAYMENT)
        ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
    }
}