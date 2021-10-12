package com.custom.rgs_android_dom.data.network.interceptors

import com.custom.rgs_android_dom.data.network.error.MSDNetworkErrorResponse
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.custom.rgs_android_dom.utils.logException
import com.google.gson.Gson
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthTokenInterceptor : Interceptor, KoinComponent {

    private val registrationRepository: RegistrationRepository by inject()

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val AUTHORIZATION_BEARER = "Bearer"
        private val ERROR_CODE_TOKEN_EXPIRED = arrayOf("AUTH-016")
    }

    private val noAuthorizationPaths = listOf(
        "/api/auth/clients/code",
        "/api/auth/clients/login",
        "/api/auth/token/refresh"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        return if (isAuthorizationNotRequired(originalRequest)) {
            chain.proceed(originalRequest)
        } else {
            val response = chain.proceed(
                originalRequest.newBuilder()
                    .apply {
                        registrationRepository.getAuthToken()?.let { authToken ->
                            header(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER $authToken")
                        }
                    }
                    .build()
            )

            if (response.isSuccessful) {
                return response
            } else {
                // TODO Finally improve this after we will have all info about tokens and auth error codes
                response.body.use { body ->
                    val responseString = body?.string() ?: ""
                    val errorResponse = parseError(responseString, response.code)

                    if (errorResponse.code in ERROR_CODE_TOKEN_EXPIRED) {
                        //registrationRepository.deleteTokens()
                        refreshToken()

                        return chain.proceed(
                            originalRequest.newBuilder()
                                .apply {
                                    registrationRepository.getAuthToken()?.let { authToken ->
                                        header(
                                            AUTHORIZATION_HEADER,
                                            "$AUTHORIZATION_BEARER $authToken"
                                        )
                                    }
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
        val refreshTokenExpiresAt = registrationRepository.getRefreshTokenExpiresAt()
        if (refreshTokenExpiresAt?.isBeforeNow == true) {
            registrationRepository.clearAuth()
        } else {
            registrationRepository.getRefreshToken()?.let { refreshToken ->
                synchronized(this) {
                    try {
                        registrationRepository.refreshToken("$AUTHORIZATION_BEARER $refreshToken")
                            .subscribeBy(onError = {
                                registrationRepository.clearAuth()
                            })
                    } catch (e: Exception) {
                        logException(this, e)
                    }
                }
            }
        }
    }


    private fun parseError(errorResponse: String, errorCode: Int): MSDNetworkErrorResponse {
        if (errorCode == 401) {
            return MSDNetworkErrorResponse("AUTH-016", "token expired", "")
        }
        return try {
            Gson().fromJson(errorResponse, MSDNetworkErrorResponse::class.java)
        } catch (e: Exception) {
            MSDNetworkErrorResponse("", e.message ?: "", "")
        }
    }

}

