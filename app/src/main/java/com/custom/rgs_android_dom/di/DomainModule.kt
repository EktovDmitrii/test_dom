package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.address.AddressInteractor
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {

    factory { CountriesInteractor(countriesRepository = get()) }

    factory { RegistrationInteractor(registrationRepository = get()) }

    factory { ClientInteractor(clientRepository = get(), registrationRepository = get(), filesRepository = get()) }

    factory { TranslationInteractor(translationRepository = get()) }

    factory { ChatInteractor(chatRepository = get(), authContentProviderManager = get()) }

    factory { PropertyInteractor(propertyRepository = get(), context = androidContext()) }

    factory { AddressInteractor(addressRepository = get()) }

    factory { CatalogInteractor(catalogRepository = get()) }
}
