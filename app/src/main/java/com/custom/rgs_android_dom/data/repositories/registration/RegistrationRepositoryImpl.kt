package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.requests.GetCodeRequest
import com.custom.rgs_android_dom.data.network.requests.LoginRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate
import java.util.*

class RegistrationRepositoryImpl(private val api: MSDApi,
                                 private val authSharedPreferences: AuthSharedPreferences) : RegistrationRepository {

    private val logout = BehaviorRelay.create<Unit>()

    override fun getCode(phone: String): Completable {
        return api.postGetCode(GetCodeRequest(phone = phone.formatPhoneForApi()))
            .flatMapCompletable {
                authSharedPreferences.saveAccessToken(it.token)
                Completable.complete()
            }
    }

    override fun login(phone: String, code: String): Single<Boolean> {
        return api.postLogin(LoginRequest(phone = phone.formatPhoneForApi(), code = code))
            .map { authResponse->
                authSharedPreferences.saveToken(authResponse.token)
                return@map authResponse.isNewUser
            }
    }

    override fun getAuthToken(): String? {
        return authSharedPreferences.getAccessToken()
    }

    override fun logout(): Completable {
        return api.postLogout().andThen {
            authSharedPreferences.clear()
            logout.accept(Unit)
        }
    }

    override fun getLogoutSubject(): BehaviorRelay<Unit> {
        return logout
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

}