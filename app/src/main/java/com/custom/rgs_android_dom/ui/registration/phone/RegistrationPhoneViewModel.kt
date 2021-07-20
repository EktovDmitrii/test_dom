package com.custom.rgs_android_dom.ui.registration.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.ScreenScope
import com.custom.rgs_android_dom.ui.registration.code.RegistrationCodeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.inject

class RegistrationPhoneViewModel(private val countriesInteractor: CountriesInteractor,
                                 private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val isNextTextViewEnabledController = MutableLiveData<Boolean>()
    private val countryController = MutableLiveData<CountryModel>()
    private var phone: String = ""

    val isNextTextViewEnabledObserver: LiveData<Boolean> = isNextTextViewEnabledController
    val countryObserver: LiveData<CountryModel> = countryController

    init {
        loadDefaultCountry()
    }

    fun onCountryClick(countryCode: String){

    }

    fun onNextClick(){
        sendPhone(phone)
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onPhoneChanged(phone: String, isMaskFilled: Boolean) {
        isNextTextViewEnabledController.value = isMaskFilled
        this.phone = phone
    }

    fun onDoneClick(){
        val isNextTextViewEnabled = isNextTextViewEnabledObserver.value ?: false
        if (isNextTextViewEnabled){
            sendPhone(phone)
        }
    }

    private fun loadDefaultCountry(){
        countriesInteractor.getDefaultCountry()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .subscribeBy(
                onSuccess = {
                    countryController.value = it
                },
                onError = {
                    //todo обработка ошибки
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun sendPhone(phone: String) {
        registrationInteractor.sendPhone(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    ScreenManager.showScreenScope(RegistrationCodeFragment.newInstance(phone), REGISTRATION)
                },
                onError = {
                    loadingStateController.value = LoadingState.CONTENT
                    //todo обработка ошибки
                }
            ).addTo(dataCompositeDisposable)
    }
}