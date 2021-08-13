package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.fill_profile.RegistrationFillProfileViewModel
import com.custom.rgs_android_dom.utils.tryParseDate
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate
import java.lang.Exception

class RegistrationInteractor(private val registrationRepository: RegistrationRepository){

    fun sendPhone(phone: String): Single<Boolean> {
        return registrationRepository.sendPhone(phone)
    }

    fun sendCode(code: String): Single<Boolean> {
        return registrationRepository.sendCode(code)
    }

    fun resendCode(phone: String) : Single<Boolean> {
        return registrationRepository.resendCode(phone)
    }

    fun acceptAgreement(): Single<Boolean> {
        return registrationRepository.acceptAgreement()
    }



}