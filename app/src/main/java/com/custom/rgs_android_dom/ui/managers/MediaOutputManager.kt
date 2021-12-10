package com.custom.rgs_android_dom.ui.managers

import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputModel
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputType

class MediaOutputManager(private val context: Context) {

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
        when (mediaOutput.type){
            MediaOutputType.PHONE -> {

            }
            MediaOutputType.WIRED_HEADPHONE -> {

            }
            MediaOutputType.BLUETOOTH -> {

            }
            MediaOutputType.SPEAKERPHONE -> {

            }
        }
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

}