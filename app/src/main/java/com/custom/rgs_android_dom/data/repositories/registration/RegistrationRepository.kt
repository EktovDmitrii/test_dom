package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.models.AuthModel
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

interface RegistrationRepository {

    fun requestCode(phone: String): Completable

    fun sendCode(phone: String, code: String): Single<AuthModel>

    fun resendCode(phone: String): Single<Boolean>

    fun acceptAgreement(): Single<Boolean>

    fun updateProfile(phone: String, name: String?, surname: String?, birthday: LocalDate?, gender: Gender?, agentCode: String?, agentPhone: String?): Single<Boolean>

    fun saveAuth(auth: AuthModel)
}