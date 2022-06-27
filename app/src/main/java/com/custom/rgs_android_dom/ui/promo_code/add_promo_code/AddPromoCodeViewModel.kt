package com.custom.rgs_android_dom.ui.promo_code.add_promo_code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class AddPromoCodeViewModel : BaseViewModel() {

    private val promoCodeController = MutableLiveData<String>()
    val promoCodeObserver: LiveData<String> = promoCodeController

    fun setTextPromoCode(text: String) {
        promoCodeController.value = text
    }
}
