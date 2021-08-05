package com.custom.rgs_android_dom.ui.registration.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toNetworkException
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.countries.CountriesFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.code.RegistrationCodeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationPhoneViewModel(
    private val countriesInteractor: CountriesInteractor,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val isNextTextViewEnabledController = MutableLiveData<Boolean>()
    private val countryController = MutableLiveData<CountryModel>()
    private val phoneErrorController = MutableLiveData<String>()

    val isNextTextViewEnabledObserver: LiveData<Boolean> = isNextTextViewEnabledController
    val countryObserver: LiveData<CountryModel> = countryController
    val phoneErrorObserver: LiveData<String> = phoneErrorController

    private var phone: String = ""

    init {
        loadDefaultCountry()
        subscribeSelectedCountrySubject()
    }

    fun onCountryClick(countryCode: String){
        ScreenManager.showScreen(CountriesFragment.newInstance(countryCode))
    }

    fun onNextClick(){
        requestCode(phone)
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onPhoneChanged(phone: String, isMaskFilled: Boolean) {
        isNextTextViewEnabledController.value = isMaskFilled
        phoneErrorController.value = ""
        this.phone = phone
    }

    fun onDoneClick(){
        val isNextTextViewEnabled = isNextTextViewEnabledObserver.value ?: false
        if (isNextTextViewEnabled){
            requestCode(phone)
        }
    }

    private fun loadDefaultCountry(){
        countriesInteractor.getDefaultCountry()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
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

    private fun requestCode(phone: String) {
        registrationInteractor.requestCode(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.value = LoadingState.LOADING
                phoneErrorController.value = ""
            }
            .doOnComplete{ loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onComplete = {
                    ScreenManager.showScreenScope(RegistrationCodeFragment.newInstance(phone), REGISTRATION)
                },
                onError = {
                    //todo обработка ошибки
                    val errorMessage = it.toNetworkException()?.message ?: "Проверьте, правильно ли вы ввели номер телефона"
                    phoneErrorController.value = errorMessage
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun subscribeSelectedCountrySubject(){
        countriesInteractor.getSelectedCountrySubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    countryController.value = it
                }
            ).addTo(dataCompositeDisposable)
    }
}