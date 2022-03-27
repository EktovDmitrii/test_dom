package com.custom.rgs_android_dom.ui.registration.code

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementFragment
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationCodeViewModel(
    private val phone: String,
    private var token: String,
    private val registrationInteractor: RegistrationInteractor,
    private val clientInteractor: ClientInteractor
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
        phoneController.value = "${TranslationInteractor.getTranslation("app.registration.code.phone_subtitle")}\n${phone}"
        startCountdownTimer()
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onResendCodeClick(){
        registrationInteractor.getCode(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess{ loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onSuccess = { token->
                    this.token = token
                    startCountdownTimer()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCodeComplete(code: String){
        registrationInteractor.login(phone, code, token)
            .flatMap { clientInteractor.getClient() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .subscribeBy(
                onSuccess = {
                    saveClientAndClose(it.isOpdSigned)
                    /*else {
                        ScreenManager.showScreen(MainFragment())
                    }*/
                },
                onError = {
                    logException(this, it)
                    codeErrorController.value = "Неправильный код"
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
                countdownTextController.value = TranslationInteractor.getTranslation("app.registration.code.timer_subtitle")
                    .replace("%@", "00:${secondsLeft}")
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

    private fun saveClientAndClose(isOpdSigned: Boolean){
        clientInteractor.loadAndSaveClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                closeController.value = Unit
                if (!isOpdSigned){
                    YandexMetrica.reportEvent("login_success_reg")
                    ScreenManager.showScreenScope(RegistrationAgreementFragment.newInstance(phone), REGISTRATION)
                } else {
                    YandexMetrica.reportEvent("session_start")
                    clientInteractor.finishAuth()
                }
            }
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }


}