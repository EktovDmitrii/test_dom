package com.custom.rgs_android_dom.data.repositories

import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.MyServiceDomApi
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import io.reactivex.Single

class CountriesRepository(private val api: MyServiceDomApi) {

    fun getDefaultCountry(): Single<CountryModel> {
        return Single.fromCallable {
            CountryModel(
                id = 1,
                name = "Россия",
                image = R.drawable.flag_ru,
                letterCode = "RU",
                numberCode = "+7",
                isCurrent = true,
                mask = "+7 [000] [000]-[00]-[00]"
            )
        }
    }

}