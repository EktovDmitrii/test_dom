package com.custom.rgs_android_dom.data.providers.auth.manager

import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.util.Log
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_ACCESS_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_IS_AUTHORIZED
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_REFRESH_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT
import com.custom.rgs_android_dom.data.providers.auth.MSDAuthContentProvider
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants
import com.custom.rgs_android_dom.data.providers.auth.constants.UriHelper
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AuthContentProviderManager(private val context: Context) {

    companion object {
        private const val PATTERN_DATE_TIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    }

    val authStateSubject = PublishSubject.create<AuthState>()

    private val isAuthorizedUris = UriHelper.urisForEndpoint(AuthContentProviderConstants.ENDPOINT_IS_AUTHORIZED)
    private val accessTokenUris = UriHelper.urisForEndpoint(AuthContentProviderConstants.ENDPOINT_ACCESS_TOKEN)
    private val refreshTokenUris = UriHelper.urisForEndpoint(AuthContentProviderConstants.ENDPOINT_REFRESH_TOKEN)
    private val refreshTokenExpiresAtUris = UriHelper.urisForEndpoint(AuthContentProviderConstants.ENDPOINT_REFRESH_TOKEN_EXPIRES_AT)
    private val actionUris = UriHelper.urisForEndpoint(AuthContentProviderConstants.ENDPOINT_ACTIONS)

    private val contentObserver = object : ContentObserver(null) {
        override fun onChange(self: Boolean) {
            if (isAuthorized()){
                authStateSubject.onNext(AuthState.AUTH_SAVED)
            } else {
                authStateSubject.onNext(AuthState.AUTH_CLEARED)
            }
        }
    }

    init {
        registerObserver()
    }

    fun isAuthorized(): Boolean {
        for (uri in isAuthorizedUris){
            try {
                val result = context.contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                )
                if (result != null){
                    result.moveToFirst()
                    val isAuthorized =  result.getString(result.getColumnIndex(CONTENT_KEY_IS_AUTHORIZED)).toBoolean()
                    result.close()
                    return isAuthorized
                }
            } catch (e: Exception){

            }
        }
        return false
    }

    fun getAccessToken(): String? {
        for (uri in accessTokenUris){
            try {
                val result = context.contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                ).apply {
                    this?.moveToFirst()
                }
                if (result != null){
                    val accessToken = result.getString(result.getColumnIndex(CONTENT_KEY_ACCESS_TOKEN))

                    result.close()
                    return accessToken
                }
            } catch (e: Exception){

            }
        }
        return null
    }

    fun getRefreshToken(): String? {
        for (uri in refreshTokenUris){
            try {
                val result = context.contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                ).apply {
                    this?.moveToFirst()
                }
                if (result != null){
                    val refreshToken = result.getString(result.getColumnIndex(CONTENT_KEY_REFRESH_TOKEN))
                    result.close()
                    return refreshToken
                }
            }catch (e: Exception){

            }
        }
        return null

    }

    fun getRefreshTokenExpiresAt(): DateTime? {
        for (uri in refreshTokenExpiresAtUris){
            try {
                val result = context.contentResolver.query(
                    MSDAuthContentProvider.URI_REFRESH_TOKEN_EXPIRES_AT,
                    null,
                    null,
                    null,
                    null
                ).apply {
                    this?.moveToFirst()
                }
                if (result != null){
                    val refreshTokenExpiresAtString = result.getString(result.getColumnIndex(CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT))

                    result.close()

                    return if (refreshTokenExpiresAtString != null) {
                        DateTime.parse(refreshTokenExpiresAtString, DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS))
                    } else {
                        null
                    }
                }
            } catch (e: Exception){

            }
        }
        return null
    }

    fun saveAuth(tokenResponse: TokenResponse){
        val values = ContentValues().apply {
            put(CONTENT_KEY_ACCESS_TOKEN, tokenResponse.accessToken)
            put(CONTENT_KEY_REFRESH_TOKEN, tokenResponse.refreshToken)
            put(
                CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT,
                tokenResponse.refreshTokenExpiresAt.toString(DateTimeFormat.forPattern(
                    PATTERN_DATE_TIME_MILLIS
                ))
            )
        }
        for (uri in actionUris){
            try {
                context.contentResolver.insert(uri, values)
                context.contentResolver.notifyChange(uri, null)
            } catch (e: Exception){

            }
        }

    }

    fun clear(){
        for (uri in actionUris){
            try {
                context.contentResolver.delete(uri, null, null)
                context.contentResolver.notifyChange(uri, null)
            }catch (e: Exception){

            }
        }
    }

    private fun registerObserver(){
        for (uri in actionUris) {
            try {
                if (uri != MSDAuthContentProvider.URI_ACTIONS){
                    context.contentResolver.registerContentObserver(uri, true, contentObserver)
                }
            } catch (e: Exception){

            }
        }
    }

}