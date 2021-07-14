package com.custom.rgs_android_dom.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.koin.core.component.KoinComponent

// TODO This is mocked data, just for example. We will change this when backend will be ready
class AuthTokenInterceptor : Interceptor, KoinComponent {

    companion object {
        private const val HEADER_TOKEN = "token"
        private val ERROR_CODE_TOKEN_EXPIRED = arrayOf(1110, 1120, 1130, 1140)
        private val ERROR_CODE_REFRESH_TOKEN_EXPIRED = arrayOf(1230)
    }

    private val noAuthorizationPaths = listOf(
        "/api/registration",
        "/api/token"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        return if (isAuthorizationNotRequired(originalRequest)) {
            chain.proceed(originalRequest)
        }
        else {
            val token = getToken()
            if (token == null) {
                tryRefreshTokenToken()
            }
            val response = chain.proceed(
                originalRequest.newBuilder()
                    .apply {
                        getToken()?.let { header(HEADER_TOKEN, it) }
                    }
                    .build()
            )
            if (response.isSuccessful) {
                return response
            } else {
                // TODO Added handling wrong token logic
                response.body.use { body ->
                    val responseString = body?.string() ?: ""
                    response.newBuilder()
                        .body(responseString.toResponseBody(body?.contentType()))
                        .build()
                }
            }
        }
    }

    private fun isAuthorizationNotRequired(request: Request): Boolean {
        return request.url.encodedPath in noAuthorizationPaths
    }

    @Synchronized
    private fun tryRefreshTokenToken() {
        synchronized(this) {
        }
    }

    private fun getToken(): String? {
        return ""
    }

}

