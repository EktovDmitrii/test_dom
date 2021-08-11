package com.custom.rgs_android_dom.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.custom.rgs_android_dom.data.network.responses.AuthResponse
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.utils.getSharedPrefs
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AuthSharedPreferences(val context: Context) {

    companion object {
        private const val PATTERN_DATE_TIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"

        private const val PREFS_NAME = "AuthSharedPreferences"
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_CLIENT_ID = "PREF_KEY_CLIENT_ID"
        private const val PREF_KEY_REFRESH_TOKEN_EXPIRES_AT = "PREF_KEY_REFRESH_TOKEN_EXPIRES_AT"
    }

    private val preferences = context.getSharedPrefs(PREFS_NAME)
    private val rxPreferences = RxSharedPreferences.create(preferences)


    fun saveAuth(authResponse: AuthResponse){
        preferences.edit{
            putString(PREF_KEY_ACCESS_TOKEN, authResponse.token.accessToken)
            putString(PREF_KEY_REFRESH_TOKEN, authResponse.token.refreshToken)
            putString(PREF_KEY_CLIENT_ID, authResponse.clientId)
            putString(PREF_KEY_REFRESH_TOKEN_EXPIRES_AT, authResponse.token.refreshTokenExpiresAt.toString(DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS)))
        }
    }

    fun saveToken(tokenResponse: TokenResponse){
        preferences.edit{
            putString(PREF_KEY_ACCESS_TOKEN, tokenResponse.accessToken)
            putString(PREF_KEY_REFRESH_TOKEN, tokenResponse.refreshToken)
            putString(PREF_KEY_REFRESH_TOKEN_EXPIRES_AT, tokenResponse.refreshTokenExpiresAt.toString(DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS)))
        }
    }

    fun getAccessToken(): String? {
        return preferences.getString(PREF_KEY_ACCESS_TOKEN, null)
    }

    fun getAccessTokenObservable(): Observable<String> {
        return rxPreferences.getString(PREF_KEY_ACCESS_TOKEN).asObservable()
    }

    fun getRefreshToken(): String? {
        return preferences.getString(PREF_KEY_REFRESH_TOKEN, null)
    }

    fun getRefreshTokenObservable(): Observable<String>{
        return rxPreferences.getString(PREF_KEY_REFRESH_TOKEN).asObservable()
    }

    fun getClientId(): String? {
        return preferences.getString(PREF_KEY_CLIENT_ID, null)
    }

    fun getRefreshTokenExpiresAt(): DateTime? {
        val refreshTokenExpiresAtString = preferences.getString(PREF_KEY_REFRESH_TOKEN_EXPIRES_AT, null)
        return if (refreshTokenExpiresAtString != null) {
            DateTime.parse(refreshTokenExpiresAtString, DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS))
        } else {
            null
        }
    }

    fun deleteTokens(){
        preferences.edit {
            remove(PREF_KEY_ACCESS_TOKEN)
            remove(PREF_KEY_REFRESH_TOKEN)
            remove(PREF_KEY_REFRESH_TOKEN_EXPIRES_AT)
        }
    }

    fun clear() {
        preferences.edit {
            clear()
        }
        rxPreferences.clear()
    }

}