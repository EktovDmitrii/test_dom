package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.repositories.DemoRepository
import com.custom.rgs_android_dom.data.repositories.chat.ChatRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.client.ClientRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepositoryMock
import com.custom.rgs_android_dom.data.repositories.location.LocationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.property.PropertyRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.translation.TranslationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.web_socket.WebSocketRepositoryImpl
import com.custom.rgs_android_dom.domain.repositories.*
import org.koin.dsl.module

val dataModule = module {
    single { DemoRepository() }
    single <RegistrationRepository> { RegistrationRepositoryImpl(api = get(), authSharedPreferences = get(), webSocketRepository = get()) }
    single <ClientRepository> {ClientRepositoryImpl(api = get(), authSharedPreferences = get())}
    single <CountriesRepository> { CountriesRepositoryMock() }
    single <TranslationRepository> { TranslationRepositoryImpl(api = get()) }
    single <ChatRepository> { ChatRepositoryImpl(api = get(), authSharedPreferences = get()) }
    single <WebSocketRepository> {WebSocketRepositoryImpl(authSharedPreferences = get(), gson = get())}
    single <PropertyRepository> { PropertyRepositoryImpl(api = get(), authSharedPreferences = get()) }
    single <LocationRepository> { LocationRepositoryImpl(context = get()) }
}