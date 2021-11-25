package com.custom.rgs_android_dom.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.utils.getSharedPrefs
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson

class ClientSharedPreferences(val context: Context, val gson: Gson) {

    companion object {
        private const val PREFS_NAME = "ClientSharedPreferences"
        private const val PREF_KEY_CLIENT_ID = "PREF_KEY_CLIENT_ID"
        private const val PREF_KEY_PHONE = "PREF_KEY_PHONE"
        private const val PREF_KEY_CLIENT = "PREF_KEY_CLIENT"
        private const val PREF_KEY_LIVEKIT_ROOM_TOKEN = "PREF_KEY_LIVEKIT_ROOM_TOKEN"
    }

    private val preferences = context.getSharedPrefs(PREFS_NAME)
    private val rxPreferences = RxSharedPreferences.create(preferences)

    fun savePhone(phone: String){
        preferences.edit{
            putString(PREF_KEY_PHONE, phone)
        }
    }

    fun getPhone(): String? {
        return preferences.getString(PREF_KEY_PHONE, null)
    }

    fun saveClient(client: ClientModel){
        preferences.edit {
            putString(PREF_KEY_CLIENT, gson.toJson(client))
        }
    }

    fun getClient(): ClientModel? {
        val clientString = preferences.getString(PREF_KEY_CLIENT, null)
        if (clientString != null){
            return gson.fromJson(clientString, ClientModel::class.java)
        }
        return null
    }

    fun saveLiveKitRoomToken(token: String){
        preferences.edit {
            putString(PREF_KEY_LIVEKIT_ROOM_TOKEN, token)
        }
    }

    fun getLiveKitRoomToken(): String?{
        return preferences.getString(PREF_KEY_LIVEKIT_ROOM_TOKEN, null)
    }

    fun removeLiveKitRoomToken(){
        preferences.edit {
            remove(PREF_KEY_LIVEKIT_ROOM_TOKEN)
        }
    }

    fun clear() {
        preferences.edit {
            clear()
        }
        rxPreferences.clear()
    }
}