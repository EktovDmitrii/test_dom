package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.db.MSDDatabase
import com.custom.rgs_android_dom.data.repositories.chat.ChatRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.client.ClientRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepositoryMock
import com.custom.rgs_android_dom.data.repositories.address.AddressRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.catalog.CatalogRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.files.FilesRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.notifications.NotificationsRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.policies.PoliciesRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.promo_codes.PromoCodesRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.property.PropertyRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.purchase.PurchaseRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.translation.TranslationRepositoryImpl
import com.custom.rgs_android_dom.domain.repositories.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<RegistrationRepository> { RegistrationRepositoryImpl(api = get(), clientSharedPreferences = get(), chatRepository = get(), authContentProviderManager = get(), database = get()) }
    single<ClientRepository> { ClientRepositoryImpl(api = get(), clientSharedPreferences = get()) }
    single<CountriesRepository> { CountriesRepositoryMock() }
    single<TranslationRepository> { TranslationRepositoryImpl(api = get()) }
    single<ChatRepository> { ChatRepositoryImpl(api = get(), clientSharedPreferences = get(), gson = get(), authContentProviderManager = get(), context = get(), mediaOutputManager = get(), connectivityManager = get(), database = get()) }
    single<PropertyRepository> { PropertyRepositoryImpl(api = get(), clientSharedPreferences = get()) }
    single<AddressRepository> { AddressRepositoryImpl(api = get()) }
    single<FilesRepository> { FilesRepositoryImpl(api = get()) }
    single<CatalogRepository> { CatalogRepositoryImpl(api = get(), authContentProviderManager = get()) }
    single<PurchaseRepository> { PurchaseRepositoryImpl(api = get()) }
    single<PoliciesRepository> { PoliciesRepositoryImpl(api = get()) }
    single { MSDDatabase.create(androidContext()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl() }
    single<PromoCodesRepository> { PromoCodesRepositoryImpl(api = get()) }
}