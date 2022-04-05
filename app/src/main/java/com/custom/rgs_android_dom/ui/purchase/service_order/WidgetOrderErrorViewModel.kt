package com.custom.rgs_android_dom.ui.purchase.service_order

import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class WidgetOrderErrorViewModel(
    val chatInteractor: ChatInteractor
) : BaseViewModel() {

    fun navigateChat() {
        ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
    }

}