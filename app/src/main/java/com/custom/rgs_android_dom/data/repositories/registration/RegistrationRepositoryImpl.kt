package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.requests.GetCodeRequest
import com.custom.rgs_android_dom.data.network.requests.LoginRequest
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime

class RegistrationRepositoryImpl(
    private val api: MSDApi,
    private val authSharedPreferences: AuthSharedPreferences
) : RegistrationRepository {

    companion object {
        private const val HEADER_BEARER = "Bearer"
    }

    private val logout = PublishSubject.create<Unit>()

    override fun getCurrentPhone(): String {
        return authSharedPreferences.getPhone() ?: ""
    }

    override fun getCode(phone: String): Single<String> {
        return api.postGetCode(GetCodeRequest(phone = phone.formatPhoneForApi()))
            .map {
                it.token
            }.doOnSubscribe { authSharedPreferences.savePhone(phone) }
    }

    override fun login(phone: String, code: String, token: String): Single<Boolean> {
        return api.postLogin(
            "$HEADER_BEARER $token",
            LoginRequest(phone = phone.formatPhoneForApi(), code = code)
        )
            .map { authResponse ->
                authSharedPreferences.saveAuth(authResponse)
                return@map authResponse.isNewUser
            }
    }

    override fun getAuthToken(): String? {
        return authSharedPreferences.getAccessToken()
    }

    override fun logout(): Completable {
        return api.postLogout().doFinally {
            if (isAuthorized()){
                authSharedPreferences.clear()
                logout.onNext(Unit)
            }
        }
    }

    override fun clearAuth() {
        if (isAuthorized()){
            authSharedPreferences.clear()
            logout.onNext(Unit)
        }
    }

    override fun getLogoutSubject(): PublishSubject<Unit> {
        return logout
    }

    override fun signOpd(clientId: String): Completable {
        return api.postSignOpd(clientId)
    }

    override fun getClientId(): String? {
        return authSharedPreferences.getClientId()
    }

    override fun refreshToken(refreshToken: String): Completable {
        return api.postRefreshToken(refreshToken).flatMapCompletable { tokenResponse ->
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

    override fun isAuthorized(): Boolean {
        return authSharedPreferences.isAuthorized()
    }

    override fun setMockToken() {
        authSharedPreferences.saveToken(TokenResponse("0", "jaskdjkasjdklajkdlj", DateTime.now().plusDays(2), "jaskldjalkdla",DateTime.now().plusDays(2)))
    }

}