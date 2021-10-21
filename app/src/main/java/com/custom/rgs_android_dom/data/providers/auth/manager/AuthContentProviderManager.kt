package com.custom.rgs_android_dom.data.providers.auth.manager

import android.content.ContentValues
import android.content.Context
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_ACCESS_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_IS_AUTHORIZED
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_REFRESH_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT
import com.custom.rgs_android_dom.data.providers.auth.MSDAuthContentProvider
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AuthContentProviderManager(private val context: Context) {

    companion object {
        private const val PATTERN_DATE_TIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    }

    private val isAuthorizedUris = arrayListOf(MSDAuthContentProvider.URI_IS_AUTHORIZED)
    private val accessTokenUris = arrayListOf(MSDAuthContentProvider.URI_ACCESS_TOKEN)
    private val refreshTokenUris = arrayListOf(MSDAuthContentProvider.URI_REFRESH_TOKEN)
    private val refreshTokenExpiresAtUris = arrayListOf(MSDAuthContentProvider.URI_REFRESH_TOKEN_EXPIRES_AT)
    private val actionUris = arrayListOf(MSDAuthContentProvider.URI_ACTIONS)

    fun isAuthorized(): Boolean {
        for (uri in isAuthorizedUris){
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
        }
        return false
    }

    fun getAccessToken(): String? {
        for (uri in accessTokenUris){
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
        }
        return null
    }

    fun getRefreshToken(): String? {
        for (uri in refreshTokenUris){
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
        }
        return null

    }

    fun getRefreshTokenExpiresAt(): DateTime? {
        for (uri in refreshTokenExpiresAtUris){
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
            context.contentResolver.insert(uri, values)
        }

    }

    fun clear(){
        for (uri in actionUris){
            context.contentResolver.delete(uri, null, null)
        }
    }

}