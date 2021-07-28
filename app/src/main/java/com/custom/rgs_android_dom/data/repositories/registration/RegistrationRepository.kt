package com.custom.rgs_android_dom.data.repositories.registration

import io.reactivex.Single

interface RegistrationRepository {

    fun sendPhone(phone: String): Single<Boolean>

    fun sendCode(code: String): Single<Boolean>

    fun resendCode(phone: String): Single<Boolean>

    fun acceptAgreement(): Single<Boolean>

}