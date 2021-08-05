package com.custom.rgs_android_dom.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.custom.rgs_android_dom.domain.registration.models.AuthModel
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


    fun saveAuth(auth: AuthModel){
        preferences.edit{
            putString(PREF_KEY_ACCESS_TOKEN, auth.accessToken)
            putString(PREF_KEY_REFRESH_TOKEN, auth.refreshToken)
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