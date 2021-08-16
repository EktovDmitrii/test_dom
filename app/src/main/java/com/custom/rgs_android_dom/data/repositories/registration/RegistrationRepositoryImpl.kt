package com.custom.rgs_android_dom.data.repositories.registration

import android.util.Log
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.requests.GetCodeRequest
import com.custom.rgs_android_dom.data.network.requests.LoginRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class RegistrationRepositoryImpl(private val api: MSDApi,
                                 private val authSharedPreferences: AuthSharedPreferences) : RegistrationRepository {

    companion object {
        private const val HEADER_BEARER = "Bearer"
    }

    private val logout = BehaviorRelay.create<Unit>()

    override fun getCode(phone: String): Single<String> {
        return api.postGetCode(GetCodeRequest(phone = phone.formatPhoneForApi()))
            .map {
                it.token
            }
    }

    override fun login(phone: String, code: String, token: String): Single<Boolean> {
        return api.postLogin("$HEADER_BEARER $token", LoginRequest(phone = phone.formatPhoneForApi(), code = code))
            .map { authResponse->
                authSharedPreferences.saveAuth(authResponse)
                return@map authResponse.isNewUser
            }
    }

    override fun getAuthToken(): String? {
        return authSharedPreferences.getAccessToken()
    }

    override fun logout(): Completable {
        return api.postLogout().doFinally {
            authSharedPreferences.clear()
            logout.accept(Unit)
            Log.d("MyLog", "Finally logout")
        }
    }

    override fun getLogoutSubject(): BehaviorRelay<Unit> {
        return logout
    }

    override fun signOpd(clientId: String): Completable {
        return api.postSignOpd(clientId)
    }

    override fun getClientId(): String? {
        return authSharedPreferences.getClientId()
    }

    override fun refreshToken(refreshToken: String): Completable {
        return api.postRefreshToken(refreshToken).flatMapCompletable { tokenResponse->
            authSharedPreferences.saveToken(tokenResponse)
            Completable.complete()
        }
    }

    override fun deleteTokens() {
        authSharedPreferences.deleteTokens()
    }

    override fun getRefreshToken(): String? {
        return authSharedPreferences.getRefreshToken()
    }

    override fun getRefreshTokenExpiresAt(): DateTime? {
        return authSharedPreferences.getRefreshTokenExpiresAt()
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