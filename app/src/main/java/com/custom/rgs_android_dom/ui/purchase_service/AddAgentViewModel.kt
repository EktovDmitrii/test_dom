package com.custom.rgs_android_dom.ui.purchase_service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AddAgentViewModel(
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val isSaveButtonEnabledController = MutableLiveData<Boolean>()
    val isSaveButtonEnabledObserver: LiveData<Boolean> = isSaveButtonEnabledController

    private var isCodeEmpty = true
    private var isPhoneMaskFilled = false

    fun onAgentCodeChanged(agentCode: String){
        isCodeEmpty = agentCode.isEmpty()
        clientInteractor.onEditAgentCodeChanged(agentCode)

        validate()
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        isPhoneMaskFilled = isMaskFilled
        clientInteractor.onEditAgentPhoneChanged(agentPhone, isMaskFilled)

        validate()
    }

    private fun validate() {
        isSaveButtonEnabledController.value = !isCodeEmpty && isPhoneMaskFilled
    }

    fun onSaveClick(onComplete: () -> Unit, onError: () -> Unit) {
        clientInteractor.updateAgent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { onComplete() },
                onError = {
                    onError()
                    handleNetworkException(it)
                }
            ).addTo(dataCompositeDisposable)
    }

}