package com.custom.rgs_android_dom.ui.registration.agreement

import android.text.Html
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.fill_client.RegistrationFillClientFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class RegistrationAgreementViewModel(private val phone: String,
                                     private val closeAfterAccept: Boolean,
                                     private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    private val isNextTextViewEnabledController = MutableLiveData<Boolean>()
    val isNextTextViewEnabledObserver: LiveData<Boolean> = isNextTextViewEnabledController

    fun onAcceptAgreementCheckedChanged(isChecked: Boolean){
        isNextTextViewEnabledController.value = isChecked
    }

    fun onNextClick(){
       acceptAgreement()
    }

    fun onCloseClick(){
        closeController.value = Unit
        //ScreenManager.showScreen(MainFragment())
    }

    fun onBackClick(){
        closeController.value = Unit
        /*if (!closeAfterAccept){
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }*/
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
                    closeController.value = Unit
                    if (!closeAfterAccept){
                        ScreenManager.showScreenScope(RegistrationFillClientFragment.newInstance(phone),REGISTRATION)
                    }
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                }
            ).addTo(dataCompositeDisposable)
    }

                                     }