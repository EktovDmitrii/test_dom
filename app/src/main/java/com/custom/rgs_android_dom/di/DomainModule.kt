package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.address.AddressInteractor
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.fcm.NotificationsInteractor
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {

    factory { CountriesInteractor(countriesRepository = get()) }

    factory { RegistrationInteractor(registrationRepository = get()) }

    factory { ClientInteractor(clientRepository = get(), registrationRepository = get(), catalogRepository = get(), policiesRepository = get(), filesRepository = get()) }

    factory { TranslationInteractor(translationRepository = get()) }

    factory { ChatInteractor(chatRepository = get(), authContentProviderManager = get()) }

    factory { PropertyInteractor(propertyRepository = get(), filesRepository = get(), context = androidContext()) }

    factory { AddressInteractor(addressRepository = get()) }

    factory { CatalogInteractor(catalogRepository = get(), policiesRepository = get()) }

    factory { PurchaseInteractor(purchaseRepository = get(), catalogRepository = get()) }

    factory { PoliciesInteractor(policiesRepository = get(), clientRepository = get(), clientSharedPreferences = get()) }

    factory { NotificationsInteractor(notificationsRepository = get(), notificationManager = get()) }

    factory { PromoCodesInteractor(promoCodesRepository = get()) }
}