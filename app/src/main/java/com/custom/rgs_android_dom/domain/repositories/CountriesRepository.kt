package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single

interface CountriesRepository {

    fun getDefaultCountry(): Single<CountryModel>

    fun getCountries(): Single<List<CountryModel>>

    fun getSelectedCountrySubject(): BehaviorRelay<CountryModel>

    fun selectCountry(newCountry: CountryModel)

}