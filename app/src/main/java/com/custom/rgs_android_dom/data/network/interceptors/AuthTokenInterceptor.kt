package com.custom.rgs_android_dom.data.network.interceptors

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthTokenInterceptor : Interceptor, KoinComponent {

    private val registrationRepository: RegistrationRepository by inject()

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val AUTHORIZATION_BEARER = "Bearer"
        private val ERROR_CODE_TOKEN_EXPIRED = arrayOf(1110, 1120, 1130, 1140)
        private val ERROR_CODE_REFRESH_TOKEN_EXPIRED = arrayOf(1230)
    }

    private val noAuthorizationPaths = listOf(
        "/api/auth/clients/code",
        "/api/auth/clients/login",
        "/api/auth/token/refresh"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        return if (isAuthorizationNotRequired(originalRequest)){
            chain.proceed(originalRequest)
        }
        else {
            val response = chain.proceed(
                originalRequest.newBuilder()
                    .apply {
                        registrationRepository.getAuthToken()?.let {authToken->
                            header(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER $authToken")
                        }
                    }
                    .build()
            )

            if (response.isSuccessful) {
                return response
            } else {
                // TODO FIX this after we will find out how to refresh token
                return response
                response.body.use { body ->
                    val responseString = body?.string() ?: ""
                    val errorResponse = responseString.toErrorResponse()

                    //Токен доступа просрочен
                    if (errorResponse.code in ERROR_CODE_TOKEN_EXPIRED) {
                        authorizationRepository.deleteToken()
                        tryRefreshTokenToken()

                        return chain.proceed(
                            originalRequest.newBuilder()
                                .apply {
                                    authorizationRepository.getToken()
                                        ?.let { header(HEADER_TOKEN, it) }
                                }
                                .build()
                        )

                    } else {
                        response.newBuilder()
                            .body(responseString.toResponseBody(body?.contentType()))
                            .build()
                    }
                }
            }
        }
    }

    private fun isAuthorizationNotRequired(request: Request): Boolean {
        return request.url.encodedPath in noAuthorizationPaths
    }

    @Synchronized
    private fun refreshToken() {
//        synchronized(this) {
//            if (authorizationRepository.getToken() == null) {
//                try {
//                    authorizationRepository.refreshTokens().blockingGet()
//                } catch (e: Exception) {
//                    Log.e("OkHttp", e.toString())
//                }
//            }
//        }
    }

}

