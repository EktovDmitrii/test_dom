package com.custom.rgs_android_dom.ui.registration.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.inject

class RegistrationPhoneViewModel(private val countriesInteractor: CountriesInteractor) : BaseViewModel() {

    private val isNextTextViewEnabledController = MutableLiveData<Boolean>()
    private val countryController = MutableLiveData<CountryModel>()

    val isNextTextViewEnabledObserver: LiveData<Boolean> = isNextTextViewEnabledController
    val countryObserver: LiveData<CountryModel> = countryController

    init {
        loadDefaultCountry()
    }

    fun onCountryClick(countryCode: String){

    }

    fun onNextClick(){

    }

    fun onCloseClick(){

    }

    fun onPhoneChanged(phone: String, isMaskFilled: Boolean) {
        isNextTextViewEnabledController.value = isMaskFilled
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
}