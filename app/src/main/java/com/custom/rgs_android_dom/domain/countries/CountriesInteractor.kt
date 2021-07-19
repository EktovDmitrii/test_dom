package com.custom.rgs_android_dom.domain.countries

import com.custom.rgs_android_dom.data.repositories.CountriesRepository
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import io.reactivex.Single

class CountriesInteractor(private val countriesRepository: CountriesRepository){

    fun getDefaultCountry(): Single<CountryModel> {
        return countriesRepository.getDefaultCountry()
    }

}