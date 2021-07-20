package com.custom.rgs_android_dom.ui.registration.code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationCodeViewModel(
    private val phone: String,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val phoneController = MutableLiveData<String>()
    private val startTimerController = MutableLiveData<Unit>()
    private val showResendCodeController = MutableLiveData<Unit>()
    private val errorMessageController = MutableLiveData<String>()

    val phoneObserver: LiveData<String> = phoneController
    val startTimerObserver: LiveData<Unit> = startTimerController
    val showResendCodeObserver: LiveData<Unit> = showResendCodeController
    val errorMessageObserver: LiveData<String> = errorMessageController

    init {
        phoneController.value = phone
        startTimerController.value = Unit
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onResendCodeClick(){
        registrationInteractor.resendCode(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .subscribeBy(
                onSuccess = {
                    startTimerController.value = Unit
                },
                onError = {
                    loadingStateController.value = LoadingState.CONTENT
                    errorMessageController.value = "Не удалось запросить код"
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCodeComplete(code: String){
        registrationInteractor.sendCode(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR  }
            .subscribeBy(
                onSuccess = {
                    closeController.value = Unit
                },
                onError = {

                }
            ).addTo(dataCompositeDisposable)
    }

    fun onTimerFinished(){
        showResendCodeController.value = Unit
    }


}