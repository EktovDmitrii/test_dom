package com.custom.rgs_android_dom.ui.client.payment_methods.error

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class ErrorDeletePaymentMethodViewModel(private val errorCode: String) : BaseViewModel() {

    private val errorCodeController =  MutableLiveData<String>()
    val errorCodeObserver: LiveData<String> = errorCodeController

    init {
        errorCodeController.value = TranslationInteractor.getTranslation("app.client.payment_methods.error.error_code")
            .replace("%@", " $errorCode")
    }

    fun onTryAgainClick(){
        close()
    }

    fun onContactOnlineMasterClick(){
        close()
    }

}