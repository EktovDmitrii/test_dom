package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.requests.GetCodeRequest
import com.custom.rgs_android_dom.data.network.requests.LoginRequest
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime

class RegistrationRepositoryImpl(
    private val api: MSDApi,
    private val clientSharedPreferences: ClientSharedPreferences,
    private val webSocketRepository: WebSocketRepository,
    private val authContentProviderManager: AuthContentProviderManager
) : RegistrationRepository {

    companion object {
        private const val HEADER_BEARER = "Bearer"
    }

    private val logout = PublishSubject.create<Unit>()

    override fun getCurrentPhone(): String {
        return clientSharedPreferences.getPhone() ?: ""
    }

    override fun getCode(phone: String): Single<String> {
        return api.postGetCode(GetCodeRequest(phone = phone.formatPhoneForApi()))
            .map {
                it.token
            }.doOnSubscribe { clientSharedPreferences.savePhone(phone) }
    }

    override fun login(phone: String, code: String, token: String): Single<Boolean> {
        return api.postLogin(
            "$HEADER_BEARER $token",
            LoginRequest(phone = phone.formatPhoneForApi(), code = code)
        )
            .map { authResponse ->
                clientSharedPreferences.saveClientId(authResponse.clientId)
                authContentProviderManager.saveAuth(authResponse.token)
                webSocketRepository.connect()
                return@map authResponse.isNewUser
            }
    }

    override fun getAccessToken(): String? {
        return authContentProviderManager.getAccessToken()
    }

    override fun logout(): Completable {
        return api.postLogout().doFinally {
            if (isAuthorized()){
                webSocketRepository.disconnect()
                authContentProviderManager.clear()
                logout.onNext(Unit)
            }
        }
    }

    override fun clearAuth() {
        if (isAuthorized()){
            authContentProviderManager.clear()
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
        return clientSharedPreferences.getClientId()
    }

    override fun refreshToken(refreshToken: String): Completable {
        return api.postRefreshToken(refreshToken).flatMapCompletable { tokenResponse ->
            authContentProviderManager.saveAuth(tokenResponse)
            Completable.complete()
        }
    }

    override fun deleteTokens() {
        authContentProviderManager.clear()
    }

    override fun getRefreshToken(): String? {
        return authContentProviderManager.getRefreshToken()
    }

    override fun getRefreshTokenExpiresAt(): DateTime? {
        return authContentProviderManager.getRefreshTokenExpiresAt()
    }

    override fun isAuthorized(): Boolean {
        return authContentProviderManager.isAuthorized()
    }

}