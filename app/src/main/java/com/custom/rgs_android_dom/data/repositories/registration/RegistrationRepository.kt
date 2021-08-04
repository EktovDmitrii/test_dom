package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.domain.profile.models.Gender
import io.reactivex.Single
import org.joda.time.LocalDate

interface RegistrationRepository {

    fun sendPhone(phone: String): Single<Boolean>

    fun sendCode(code: String): Single<Boolean>

    fun resendCode(phone: String): Single<Boolean>

    fun acceptAgreement(): Single<Boolean>

    fun updateProfile(phone: String, name: String?, surname: String?, birthday: LocalDate?, gender: Gender?, agentCode: String?, agentPhone: String?): Single<Boolean>

}