package com.custom.rgs_android_dom.ui.managers

import android.content.ContentValues
import android.content.Context
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.data.providers.MSDAuthContentProvider
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object AuthContentProviderManager {

    private const val PATTERN_DATE_TIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"

    fun isAuthorized(context: Context): Boolean {
        val result = context.contentResolver.query(
            MSDAuthContentProvider.URI_IS_AUTHORIZED,
            null,
            null,
            null,
            null
        ).apply {
            this?.moveToFirst()
        }

        val isAuthorized =  result?.getString(result.getColumnIndex(MSDAuthContentProvider.CONTENT_KEY_IS_AUTHORIZED)).toBoolean()
        result?.close()

        return isAuthorized

    }

    fun getAccessToken(context: Context): String? {
        val result = context.contentResolver.query(
            MSDAuthContentProvider.URI_ACCESS_TOKEN,
            null,
            null,
            null,
            null
        ).apply {
            this?.moveToFirst()
        }

        val accessToken = result?.getString(result.getColumnIndex(MSDAuthContentProvider.CONTENT_KEY_ACCESS_TOKEN))

        result?.close()
        return accessToken

    }

    fun getRefreshToken(context: Context): String? {
        val result = context.contentResolver.query(
            MSDAuthContentProvider.URI_REFRESH_TOKEN,
            null,
            null,
            null,
            null
        ).apply {
            this?.moveToFirst()
        }

        val refreshToken = result?.getString(result.getColumnIndex(MSDAuthContentProvider.CONTENT_KEY_REFRESH_TOKEN))

        result?.close()

        return refreshToken
    }

    fun getRefreshTokenExpiresAt(context: Context): DateTime? {
        val result = context.contentResolver.query(
            MSDAuthContentProvider.URI_REFRESH_TOKEN_EXPIRES_AT,
            null,
            null,
            null,
            null
        ).apply {
            this?.moveToFirst()
        }

        val refreshTokenExpiresAtString = result?.getString(result.getColumnIndex(MSDAuthContentProvider.CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT))

        result?.close()

        return if (refreshTokenExpiresAtString != null) {
            DateTime.parse(refreshTokenExpiresAtString, DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS))
        } else {
            null
        }
    }

    fun saveAuth(context: Context, tokenResponse: TokenResponse){
        val values = ContentValues().apply {
            put(MSDAuthContentProvider.CONTENT_KEY_ACCESS_TOKEN, tokenResponse.accessToken)
            put(MSDAuthContentProvider.CONTENT_KEY_REFRESH_TOKEN, tokenResponse.refreshToken)
            put(
                MSDAuthContentProvider.CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT,
                tokenResponse.refreshTokenExpiresAt.toString(DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS))
            )
        }

        context.contentResolver.insert(MSDAuthContentProvider.URI_ACTIONS, values)
    }

    fun clear(context: Context){
        context.contentResolver.delete(MSDAuthContentProvider.URI_ACTIONS, null, null)
    }

}