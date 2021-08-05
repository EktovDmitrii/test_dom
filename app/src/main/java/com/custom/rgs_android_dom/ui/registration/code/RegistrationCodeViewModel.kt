package com.custom.rgs_android_dom.ui.registration.code

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toNetworkException
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationCodeViewModel(
    private val phone: String,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    companion object {
        private const val TIMER_MILLIS: Long = 60000
    }

    private val phoneController = MutableLiveData<String>()
    private val onTimerStartController = MutableLiveData<Unit>()
    private val showResendCodeController = MutableLiveData<Unit>()
    private val countdownTextController = MutableLiveData<String>()
    private val codeErrorController = MutableLiveData<String>()
    private val otcReceivedController = MutableLiveData<String>()

    val phoneObserver: LiveData<String> = phoneController
    val onTimerStartObserver: LiveData<Unit> = onTimerStartController
    val showResendCodeObserver: LiveData<Unit> = showResendCodeController
    val countdownTextObserver: LiveData<String> = countdownTextController
    val codeErrorObserver: LiveData<String> = codeErrorController
    val otcReceivedObserver: LiveData<String> = otcReceivedController

    private var timer: CountDownTimer? = null

    init {
        phoneController.value = "Мы отправили СМС на номер\n$phone"
        startCountdownTimer()
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onResendCodeClick(){
        registrationInteractor.requestCode(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnComplete { loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onComplete = {
                    startCountdownTimer()
                },
                onError = {

                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCodeComplete(code: String){
        registrationInteractor.sendCode(phone, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .subscribeBy(
                onSuccess = {authModel->
                    registrationInteractor.saveAuth(authModel)
                    closeController.value = Unit
                    ScreenManager.showScreenScope(RegistrationAgreementFragment.newInstance(phone), REGISTRATION)
                    // If phone ends with 55 this is a mocked "registered" user
//                    if (!phone.endsWith("55")){
//                        ScreenManager.showScreenScope(RegistrationAgreementFragment.newInstance(phone), REGISTRATION)
//                    }
                },
                onError = {
                    //it.printStackTrace()
                    codeErrorController.value = it.toNetworkException()?.message ?: "Неправильный код"
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onOTCReceived(otc: String?){
        otc?.let {
            otcReceivedController.value = it
        }
    }

    private fun startCountdownTimer(){
        onTimerStartController.value = Unit
        timer?.cancel()
        timer = object : CountDownTimer(TIMER_MILLIS, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = String.format("%02d", millisUntilFinished.div(1000))
                countdownTextController.value = "Вы сможете повторно запросить\nкод через 00:$secondsLeft"
            }

            override fun onFinish() {
                onTimerFinished()
            }
        }
        timer?.start()
    }

    private fun onTimerFinished(){
        showResendCodeController.value = Unit
    }


}