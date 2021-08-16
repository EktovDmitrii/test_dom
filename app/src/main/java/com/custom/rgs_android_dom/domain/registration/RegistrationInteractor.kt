package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.fill_profile.RegistrationFillProfileViewModel
import com.custom.rgs_android_dom.utils.tryParseDate
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate
import java.lang.Exception

class RegistrationInteractor(private val registrationRepository: RegistrationRepository){

    fun getCode(phone: String): Single<String> {
        return registrationRepository.getCode(phone)
    }

    fun login(phone: String, code: String, token: String): Single<Boolean> {
        return registrationRepository.login(phone, code, token)
    }

    fun signOpd(clientId: String): Completable {
        return registrationRepository.signOpd(clientId)
    }

    fun getClientId(): String? {
        return registrationRepository.getClientId()
    }

    fun logout(): Completable {
        return registrationRepository.logout()
    }

    fun getLogoutSubject(): BehaviorRelay<Unit>{
        return registrationRepository.getLogoutSubject()
    }



}