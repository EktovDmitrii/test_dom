package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.data.network.MyServiceDomApi
import com.custom.rgs_android_dom.data.network.mappers.RegistrationMapper
import com.custom.rgs_android_dom.data.network.requests.AuthRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.models.AuthModel
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate
import java.util.*

class RegistrationRepositoryImpl(private val api: MyServiceDomApi,
                                 private val authSharedPreferences: AuthSharedPreferences) : RegistrationRepository {

    override fun requestCode(phone: String): Completable {
        return api.postRequestCode(phone.formatPhoneForApi())
    }

    override fun sendCode(phone: String, code: String): Single<AuthModel> {
        val request = AuthRequest(username = phone.formatPhoneForApi(), authCode = code)
        return api.postLogin(request).map { authResponse->
            RegistrationMapper.responseToAuthModel(authResponse)
        }
    }

    override fun resendCode(phone: String): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            if (phone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun acceptAgreement(): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            true
        }
    }

    override fun updateProfile(
        phone: String,
        name: String?,
        surname: String?,
        birthday: LocalDate?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(3000)
            val agentPhone = agentPhone
            if (agentPhone != null && agentPhone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun saveAuth(auth: AuthModel) {
        authSharedPreferences.saveAuth(auth)
    }

}