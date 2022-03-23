package com.custom.rgs_android_dom.ui.registration.agreement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.registration.fill_client.RegistrationFillClientFragment
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationAgreementViewModel(private val phone: String,
                                     private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    private val isNextTextViewEnabledController = MutableLiveData<Boolean>()
    val isNextTextViewEnabledObserver: LiveData<Boolean> = isNextTextViewEnabledController

    private val isSignedBeforeCloseController = MutableLiveData<Boolean>()
    val isSignedBeforeCloseObserver: LiveData<Boolean> = isSignedBeforeCloseController

    private var isAcceptedAgreement = false

    fun onAcceptAgreementCheckedChanged(isChecked: Boolean){
        isNextTextViewEnabledController.value = isChecked
    }

    fun onNextClick(){
       acceptAgreement()
    }

    fun onBackClick(){
        if (!isAcceptedAgreement) {
            registrationInteractor.logout(TargetScreen.REGISTRATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = {
                        YandexMetrica.reportEvent("session_start_offline")
                        isSignedBeforeCloseController.value = false
                    },
                    onError = {
                        logException(this, it)
                        YandexMetrica.reportEvent("session_start_offline")
                        isSignedBeforeCloseController.value = false
                    }
                )
                .addTo(dataCompositeDisposable)
        } else {
            isSignedBeforeCloseController.value = true
        }
    }

    private fun acceptAgreement(){
        registrationInteractor.signOpd()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnComplete { loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onComplete = {
                    isAcceptedAgreement = true
                    closeController.value = Unit
                    YandexMetrica.reportEvent("session_start")

                    ScreenManager.showScreenScope(RegistrationFillClientFragment.newInstance(phone),REGISTRATION)
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                }
            ).addTo(dataCompositeDisposable)
    }

                                     }