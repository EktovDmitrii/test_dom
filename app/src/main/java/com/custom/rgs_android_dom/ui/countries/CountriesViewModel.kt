package com.custom.rgs_android_dom.ui.countries

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.countries.model.CountryPresentationModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CountriesViewModel(
    private val selectedCountryLetterCode: String,
    private val countriesInteractor: CountriesInteractor
) : BaseViewModel() {

    private val countriesController = MutableLiveData<List<CountryPresentationModel>>()
    private val isSearchInputFocusedController = MutableLiveData<Boolean>()
    private var countries = listOf<CountryPresentationModel>()

    val countriesObserver: LiveData<List<CountryPresentationModel>> = countriesController
    val isSearchInputFocusedObserver: LiveData<Boolean> = isSearchInputFocusedController


    init {
        loadCountries()
    }

    private fun loadCountries() {
        countriesInteractor.getCountries()
            .map { countries->
                countries.map {
                    Log.d("MyLog", "Selected code " + selectedCountryLetterCode)
                    CountryPresentationModel.from(it, selectedCountryLetterCode)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onSuccess = {
                    countries = it
                    countriesController.value = it
                },
                onError = {

                }
            ).addTo(dataCompositeDisposable)
    }

    fun onSearchQueryChanged(query: String){
        countriesController.value = if (query.isNotEmpty()) countries.filter { it.name.contains(query) } else countries
    }

    fun onClearClick(){
        isSearchInputFocusedController.value = false
    }

    fun onCountryClick(country: CountryPresentationModel){
        countriesInteractor.selectCountry(CountryPresentationModel.toCountryModel(country))
        closeController.value = Unit
    }

    fun onCloseClick(){
        closeController.value = Unit
    }


}