package com.custom.rgs_android_dom.ui.managers

import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.util.Log
import androidx.core.content.edit
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputModel
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputType
import com.custom.rgs_android_dom.utils.getEnum
import com.custom.rgs_android_dom.utils.getSharedPrefs
import com.custom.rgs_android_dom.utils.putEnum
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable

class MediaOutputManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "MEDIA_OUTPUT_PREFERENCES"
        private const val KEY_SELECTED_MEDIA_OUTPUT = "KEY_SELECTED_MEDIA_OUTPUT"
    }

    private val preferences = context.getSharedPrefs(PREFS_NAME)
    private val rxPreferences = RxSharedPreferences.create(preferences)
    private val mediaOutputPreference = rxPreferences.getEnum(KEY_SELECTED_MEDIA_OUTPUT, MediaOutputType.PHONE, MediaOutputType::class.java)

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun getAvailableMediaOutputs(callback: (ArrayList<MediaOutputModel>) -> Unit = {}){
        val mediaOutputs = arrayListOf<MediaOutputModel>()

        mediaOutputs.add(
            MediaOutputModel(
                name = "Телефон",
                isActive = isPhoneOutputOn(),
                type = MediaOutputType.PHONE
            )
        )
        mediaOutputs.add(
            MediaOutputModel(
                name = "Динамик",
                isActive = audioManager.isSpeakerphoneOn,
                type = MediaOutputType.SPEAKERPHONE
            )
        )
        if (isHeadsetOn()){
            mediaOutputs.add(
                MediaOutputModel(
                    name = "Наушники",
                    isActive = isHeadsetOn(),
                    type = MediaOutputType.WIRED_HEADPHONE
                )
            )
        }

        if (bluetoothManager.adapter.isEnabled){
            val serviceListener = object : BluetoothProfile.ServiceListener {

                override fun onServiceConnected(proxy: Int, profile: BluetoothProfile) {
                    for (device in profile.connectedDevices){
                        mediaOutputs.add(
                            MediaOutputModel(
                                name = device.name,
                                isActive = isBTHeadsetOn(),
                                type = MediaOutputType.BLUETOOTH
                            )
                        )
                    }
                    bluetoothManager.adapter.closeProfileProxy(proxy, profile)
                    callback(mediaOutputs)
                }

                override fun onServiceDisconnected(p0: Int) {

                }
            }

            bluetoothManager.adapter.getProfileProxy(context, serviceListener, BluetoothProfile.HEADSET)
        } else {
            callback(mediaOutputs)
        }
    }

    fun setMediaOutput(mediaOutput: MediaOutputModel){
        Log.d("MyLog", "Set media output")
        mediaOutputPreference.set(mediaOutput.type)
    }

    fun getSelectedMediaOutputType(): Observable<MediaOutputType>{
        return mediaOutputPreference.asObservable()
    }

    private fun isHeadsetOn(): Boolean {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        for (device in devices) {
            if (device.type == AudioDeviceInfo.TYPE_WIRED_HEADSET || device.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES) {
                return true
            }
        }
        return false
    }

    private fun isBTHeadsetOn(): Boolean {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        for (device in devices) {
            if (device.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP || device.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                return true
            }
        }
        return false
    }

    private fun isPhoneOutputOn(): Boolean {
        return !audioManager.isSpeakerphoneOn && !isBTHeadsetOn() && !isHeadsetOn()
    }

    private fun getSelectedMediaOutput(): MediaOutputType {
        return preferences.getEnum(KEY_SELECTED_MEDIA_OUTPUT, MediaOutputType.PHONE)
    }

}