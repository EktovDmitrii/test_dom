package com.custom.rgs_android_dom.data.repositories.countries

import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CountriesMapper
import com.custom.rgs_android_dom.data.network.responses.CountryResponse
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single

class MockCountriesRepositoryImpl() : CountriesRepository {

    private val selectedCountry = BehaviorRelay.create<CountryModel>()

    override fun getDefaultCountry(): Single<CountryModel> {
        return getCountries().map {
            it.find { it.letterCode == "RU" }
        }
    }

    override fun getCountries(): Single<List<CountryModel>> {
        return Single.fromCallable {
            mockedCountriesResponse.map {
                CountriesMapper.responseToCountry(it, flagsMap)
            }
        }
    }

    override fun getSelectedCountrySubject(): BehaviorRelay<CountryModel> {
        return selectedCountry
    }

    override fun selectCountry(newCountry: CountryModel) {
        selectedCountry.accept(newCountry)
    }

    companion object {
        private val flagsMap = mapOf<String, Int>(
            "AZ" to R.drawable.flag_az,
            "AM" to R.drawable.flag_am,
            "BY" to R.drawable.flag_by,
            "KR" to R.drawable.flag_kr,
            "KZ" to R.drawable.flag_kz,
            "MD" to R.drawable.flag_md,
            "RU" to R.drawable.flag_ru,
            "TJ" to R.drawable.flag_tj,
            "TM" to R.drawable.flag_tm,
            "UA" to R.drawable.flag_ua,
            "UZ" to R.drawable.flag_uz
        )

        // TODO This is mocked data, especially last few models. I was testing how scroller is working in countries screen
        private val mockedCountriesResponse = arrayListOf(
            CountryResponse(
                id = 1,
                name = "Азербайджан",
                letterCode = "AZ",
                numberCode = "+994",
                mask = "+994 [00] [000]-[00]-[00]"
            ),
            CountryResponse(
                id = 2,
                name = "Армения",
                letterCode = "AM",
                numberCode = "+374",
                mask = "+374 [0000]-[0000]"
            ),
            CountryResponse(
                id = 3,
                name = "Беларусь",
                letterCode = "BY",
                numberCode = "+375",
                mask = "+375 [00] [000]-[00]-[00]"
            ),
            CountryResponse(
                id = 4,
                name = "Казахстан",
                letterCode = "KZ",
                numberCode = "+7",
                mask = "+7 [000] [000]-[00]-[00]"
            ),
            CountryResponse(
                id = 5,
                name = "Киргизия",
                letterCode = "KR",
                numberCode = "+996",
                mask = "+996 [000]-[000]-[000]"
            ),
            CountryResponse(
                id = 6,
                name = "Молдова",
                letterCode = "MD",
                numberCode = "+373",
                mask = "+373 [0000]-[0000]"
            ),
            CountryResponse(
                id = 7,
                name = "Таджикистан",
                letterCode = "TJ",
                numberCode = "+992",
                mask = "+992 [000]-[00]-[00]-[00]"
            ),
            CountryResponse(
                id = 8,
                name = "Туркменистан",
                letterCode = "TM",
                numberCode = "+993",
                mask = "+993 [0000]-[00]-[00]"
            ),
            CountryResponse(
                id = 9,
                name = "Узбекистан ",
                letterCode = "UZ",
                numberCode = "+998",
                mask = "+993 [00] [000]-[00]-[00]"
            ),
            CountryResponse(
                id = 10,
                name = "Украина",
                letterCode = "UA",
                numberCode = "+380",
                mask = "+380 [000] [000]-[000]"
            )
        ).apply {
            sortBy {
                it.name
            }
            add(
                0, CountryResponse(
                    id = 11,
                    name = "Россия",
                    letterCode = "RU",
                    numberCode = "+7",
                    mask = "+7 [000] [000]-[00]-[00]"
                )
            )
        }
    }

}