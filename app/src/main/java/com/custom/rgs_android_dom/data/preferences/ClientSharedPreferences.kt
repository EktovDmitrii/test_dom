package com.custom.rgs_android_dom.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.custom.rgs_android_dom.domain.chat.models.CallJoinModel
import com.custom.rgs_android_dom.domain.client.models.ClientAgent
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
        private const val PREF_KEY_LIVEKIT_CALL_ID = "PREF_KEY_LIVEKIT_ROOM_TOKEN"
        private const val PREF_KEY_LIVEKIT_ROOM_ID = "PREF_KEY_LIVEKIT_ROOM_ID"
        private const val PREF_KEY_AGENT = "PREF_KEY_AGENT"
        private const val PREF_EDIT_AGENT_WAS_REQUESTED = "PREF_TEXT_AGENT"

        private const val PREFS_ONBOARDING = "OnboardingSharedPreference"
        private const val PREF_IS_FIRST_RUN = "PREF_IS_FIRST_RUN"
    }

    private val preferences = context.getSharedPrefs(PREFS_NAME)
    private val onboardingPreferences = context.getSharedPrefs(PREFS_ONBOARDING)
    private val rxPreferences = RxSharedPreferences.create(preferences)

    fun isFirstRun(): Boolean {
        return onboardingPreferences.getBoolean(PREF_IS_FIRST_RUN, true)
    }

    fun onFirstRun() {
        onboardingPreferences.edit {
            putBoolean(PREF_IS_FIRST_RUN, false)
        }
    }

    fun savePhone(phone: String){
        preferences.edit{
            putString(PREF_KEY_PHONE, phone)
        }
    }

    fun getPhone(): String? {
        return preferences.getString(PREF_KEY_PHONE, null)
    }

    fun saveEditAgentWasRequested(text: Boolean){
        preferences.edit(){
            putBoolean(PREF_EDIT_AGENT_WAS_REQUESTED ,text)
        }
    }

    fun getText(): Boolean {
        return preferences.getBoolean(PREF_EDIT_AGENT_WAS_REQUESTED, true)
    }


    fun saveClient(client: ClientModel){
        preferences.edit {
            putString(PREF_KEY_CLIENT, gson.toJson(client))
        }
    }

    fun getClient(): ClientModel? {
        val clientString = preferences.getString(PREF_KEY_CLIENT, null)
        if (clientString != null){
            val client = gson.fromJson(clientString, ClientModel::class.java)
            client.agent = getAgent()
            client.agent?.editAgentWasRequested = getText()
            return client
        }
        return null
    }

    fun saveAgent(agent: ClientAgent?){
        preferences.edit {
            if (agent != null){
                putString(PREF_KEY_AGENT, gson.toJson(agent))
            } else {
                remove(PREF_KEY_AGENT)
            }
        }
    }

    fun saveLiveKitRoomCredentials(callJoin: CallJoinModel){
        preferences.edit {
            putString(PREF_KEY_LIVEKIT_ROOM_TOKEN, callJoin.token)
            putString(PREF_KEY_LIVEKIT_CALL_ID, callJoin.callId)
            putString(PREF_KEY_LIVEKIT_ROOM_ID, callJoin.roomId)
        }
    }

    fun getLiveKitRoomCredentials(): CallJoinModel?{
        if (preferences.contains(PREF_KEY_LIVEKIT_ROOM_TOKEN)){
            return CallJoinModel(
                token = preferences.getString(PREF_KEY_LIVEKIT_ROOM_TOKEN, "") ?: "",
                callId = preferences.getString(PREF_KEY_LIVEKIT_CALL_ID, "") ?: "",
                roomId = preferences.getString(PREF_KEY_LIVEKIT_ROOM_ID, "") ?: ""
            )
        }
        return null
    }

    fun clearLiveKitRoomCredentials(){
        preferences.edit{
            remove(PREF_KEY_LIVEKIT_ROOM_TOKEN)
            remove(PREF_KEY_LIVEKIT_CALL_ID)
            remove(PREF_KEY_LIVEKIT_ROOM_ID)
        }
    }

    fun clear() {
        preferences.edit {
            clear()
        }
        rxPreferences.clear()
    }

    private fun getAgent(): ClientAgent? {
        val agentString = preferences.getString(PREF_KEY_AGENT, null)
        if (agentString != null){
            return gson.fromJson(agentString, ClientAgent::class.java)
        }
        return null
    }

}