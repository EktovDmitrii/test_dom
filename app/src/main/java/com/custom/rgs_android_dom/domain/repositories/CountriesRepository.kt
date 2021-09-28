package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface CountriesRepository {

    fun getDefaultCountry(): Single<CountryModel>

    fun getCountries(): Single<List<CountryModel>>

    fun getSelectedCountrySubject(): PublishSubject<CountryModel>

    fun selectCountry(newCountry: CountryModel)

}