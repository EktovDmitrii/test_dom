package com.custom.rgs_android_dom.data.repositories.countries

import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import io.reactivex.Single

interface CountriesRepository {

    fun getDefaultCountry(): Single<CountryModel>

}