package com.custom.rgs_android_dom.ui.client.agent.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class EditAgentViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController

    private val validateExceptionController = MutableLiveData<SpecificValidateClientExceptions>()
    val validateExceptionObserver: LiveData<SpecificValidateClientExceptions> = validateExceptionController

    init {
        clientInteractor.validateSubject.hide()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    isSaveTextViewEnabledController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onAgentCodeChanged(agentCode: String){
        clientInteractor.onEditAgentCodeChanged(agentCode)
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        clientInteractor.onEditAgentPhoneChanged(agentPhone, isMaskFilled)
    }

    fun onSaveClick(){
        clientInteractor.updateAgent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    onBackClick()
                },
                onError = {
                    when(it){
                        is SpecificValidateClientExceptions -> {
                            validateExceptionController.value = it
                            loadingStateController.value = LoadingState.CONTENT
                        }
                        else -> {
                            handleNetworkException(it)
                            loadingStateController.value = LoadingState.ERROR
                        }
                    }
                }
            ).addTo(dataCompositeDisposable)
    }


}