package com.custom.rgs_android_dom.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.custom.rgs_android_dom.utils.getSharedPrefs
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson

class AuthSharedPreferences(val context: Context, val gson: Gson) {

    companion object {
        private const val PREFS_NAME = "AuthSharedPreferences"
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN_EXPIRES_AT = "PREF_KEY_REFRESH_TOKEN_EXPIRES_AT"
    }

    private val preferences = context.getSharedPrefs(PREFS_NAME)
    private val rxPreferences = RxSharedPreferences.create(preferences)

    fun saveAuthCredentials(accessToken: String, refreshToken: String, refreshTokenExpiresAt: String){
        preferences.edit{
            putString(PREF_KEY_ACCESS_TOKEN, accessToken)
            putString(PREF_KEY_REFRESH_TOKEN, refreshToken)
            putString(PREF_KEY_REFRESH_TOKEN_EXPIRES_AT, refreshTokenExpiresAt)
        }
    }

    fun getAccessToken(): String? {
        return preferences.getString(PREF_KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return preferences.getString(PREF_KEY_REFRESH_TOKEN, null)
    }

    fun getRefreshTokenExpiresAt(): String? {
        return preferences.getString(PREF_KEY_REFRESH_TOKEN_EXPIRES_AT, null)
    }

    fun clear() {
        preferences.edit {
            clear()
        }
        rxPreferences.clear()
    }
}