package com.custom.rgs_android_dom.domain.countries

import com.custom.rgs_android_dom.domain.repositories.CountriesRepository
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class CountriesInteractor(private val countriesRepository: CountriesRepository){

    fun getDefaultCountry(): Single<CountryModel> {
        return countriesRepository.getDefaultCountry()
    }

    fun getCountries(): Single<List<CountryModel>>{
        return countriesRepository.getCountries()
    }

    fun selectCountry(newCountry: CountryModel){
        countriesRepository.selectCountry(newCountry)
    }

    fun getSelectedCountrySubject(): PublishSubject<CountryModel> {
        return countriesRepository.getSelectedCountrySubject()
    }

}