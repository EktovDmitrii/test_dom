package com.custom.rgs_android_dom.data.preferences

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.utils.getSharedPrefs
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable

class AuthSharedPreferences(val context: Context) {

    companion object {
        private const val PREFS_NAME = "AuthSharedPreferences"
        private const val PREF_KEY_ACCESS_TOKEN = "ARGS_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "ARGS_REFRESH_TOKEN"
        private const val PREF_KEY_IS_ANONYM = "ARGS_IS_ANONYM"
        private const val PREF_KEY_PUSH_TOKEN = "ARGS_PUSH_TOKEN"
    }

    private val preferences = context.getSharedPrefs(PREFS_NAME)
    private val rxPreferences = RxSharedPreferences.create(preferences)


    fun saveToken(token: TokenResponse){
        preferences.edit{
            putString(PREF_KEY_ACCESS_TOKEN, token.accessToken)
            putString(PREF_KEY_REFRESH_TOKEN, token.refreshToken)
        }
    }

    fun saveAccessToken(accessToken: String){
        preferences.edit {
            putString(PREF_KEY_ACCESS_TOKEN, accessToken)
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

    fun clear() {
        preferences.edit {
            clear()
        }
        rxPreferences.clear()
    }




}