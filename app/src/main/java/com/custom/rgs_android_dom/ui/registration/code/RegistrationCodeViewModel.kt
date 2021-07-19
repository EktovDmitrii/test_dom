package com.custom.rgs_android_dom.ui.registration.code

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationCodeViewModel(
    phone: String,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val setErrorStateController = MutableLiveData<Unit>()
    private val phoneController = MutableLiveData<String>()
    private val startTimerController = MutableLiveData<Unit>()
    private val showResendCodeController = MutableLiveData<Unit>()

    val setErrorStateObserver: LiveData<Unit> = setErrorStateController
    val phoneObserver: LiveData<String> = phoneController
    val startTimerObserver: LiveData<Unit> = startTimerController
    val showResendCodeObserver: LiveData<Unit> = showResendCodeController

    init {
        phoneController.value = phone
        startTimerController.value = Unit
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onResendCodeClick(){
        startTimerController.value = Unit
    }

    fun onCodeComplete(code: String){
        Log.d("MyLog", "On code complete: " + code)
        registrationInteractor.sendCode(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .subscribeBy(
                onSuccess = {

                },
                onError = {
                    //todo обработка ошибки
                    setErrorStateController.value = Unit
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onTimerFinished(){
        showResendCodeController.value = Unit
    }


}