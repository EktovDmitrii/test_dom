package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.responses.CountryResponse
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import java.util.*

object CountriesMapper {

    fun responseToCountry(response: CountryResponse, flags: Map<String, Int>): CountryModel{
        return CountryModel(
            id = response.id,
            name = response.name,
            image = flags[response.letterCode.toUpperCase(Locale.getDefault())] ?: R.drawable.flag_ru,
            letterCode = response.letterCode,
            numberCode = response.numberCode,
            mask = response.mask
        )
    }

}