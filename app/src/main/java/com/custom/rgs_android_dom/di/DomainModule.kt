package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.location.LocationInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.domain.web_socket.WebSocketInteractor
import org.koin.dsl.module

val domainModule = module {

    factory { CountriesInteractor(countriesRepository = get()) }

    factory { RegistrationInteractor(registrationRepository = get()) }

    factory { ClientInteractor(clientRepository = get(), registrationRepository = get(), countriesRepository = get()) }

    factory { TranslationInteractor(translationRepository = get()) }

    factory { ChatInteractor(chatRepository = get(), webSocketRepository = get()) }

    factory { WebSocketInteractor(webSocketRepository = get(), authSharedPreferences = get()) }

    factory { PropertyInteractor(propertyRepository = get()) }

    factory { LocationInteractor(locationRepository = get()) }
}