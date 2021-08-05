package com.custom.rgs_android_dom.ui.registration.agreement

import android.text.Html
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.fill_profile.RegistrationFillProfileFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class RegistrationAgreementViewModel(private val phone: String,
                                     private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    private val isAgreementNotAcceptedErrorController = MutableLiveData<String>()
    private val agreementTextController = MutableLiveData<CharSequence>()
    private val isNextTextViewEnabledController = MutableLiveData<Boolean>()

    val legalTextObserver: LiveData<CharSequence> = agreementTextController
    val isNextTextViewEnabledObserver: LiveData<Boolean> = isNextTextViewEnabledController

    init {
        agreementTextController.value = createAgreementText()
    }

    fun onAcceptAgreementCheckedChanged(isChecked: Boolean){
        isNextTextViewEnabledController.value = isChecked
    }

    fun onNextClick(){
       acceptAgreement()
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onBackClick(){
        closeController.value = Unit
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
    }

    private fun acceptAgreement(){
        registrationInteractor.acceptAgreement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onSuccess = {
                    closeController.value = Unit
                    ScreenManager.showScreenScope(RegistrationFillProfileFragment.newInstance(phone),REGISTRATION)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun createAgreementText(): CharSequence {
        val agreementBuilder = StringBuilder().apply{
            append("Я принимаю условия")
            append("\n<a href=\"https://moi-service.ru/legal/moi-servis-med/polzovatelskoe-soglashenie\">")
            append("пользовательского соглашения,</a>")
            append("\n<a href=\"https://moi-service.ru/legal/policy\">")
            append("политику обработки персональных данных,</a> и даю своё")
            append("\n<a href=\"https://moi-service.ru/legal/soglasie-polzovatelya-na-obrabotku-personalnyh-dannyh\">")
            append("согласие на обработку персональных данных")
        }
        return Html.fromHtml(agreementBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)
    }

}