package com.custom.rgs_android_dom.ui.managers

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioDeviceInfo
import android.media.AudioManager
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputModel
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputType
import io.reactivex.subjects.PublishSubject

class MediaOutputManager(private val context: Context) {

    companion object {
        private const val HEADSET_CONNECTED = 1
        private const val HEADSET_DISCONNECTED = 0
    }

    val selectedMediaOutputSubject = PublishSubject.create<MediaOutputType>()

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var currentMediaOutputType = MediaOutputType.PHONE

    private var wasSpeakerphoneOn = audioManager.isSpeakerphoneOn
    private var wasMicrophoneMute = audioManager.isMicrophoneMute
    private var initialAudioMode = audioManager.mode

    private var callActive = false
    private var canAutoSwitchProfile = true

    init {
        val serviceListener = object : BluetoothProfile.ServiceListener {

            override fun onServiceConnected(proxy: Int, profile: BluetoothProfile) {

            }

            override fun onServiceDisconnected(p0: Int) {
                if (callActive && currentMediaOutputType == MediaOutputType.BLUETOOTH){
                    setMediaOutput(MediaOutputType.PHONE, false)
                }
            }
        }

        bluetoothManager.adapter.getProfileProxy(context, serviceListener, BluetoothProfile.HEADSET)

        val btConnectionBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // TODO Maybe check if device == Bluetooth headset??
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when {
                    BluetoothDevice.ACTION_ACL_CONNECTED == intent.action -> {
                        if (callActive && canAutoSwitchProfile){
                            setMediaOutput(MediaOutputType.BLUETOOTH, false)
                        }
                    }
                    BluetoothDevice.ACTION_ACL_DISCONNECTED == intent.action -> {
                        if (callActive && currentMediaOutputType == MediaOutputType.BLUETOOTH){
                            setMediaOutput(MediaOutputType.PHONE, false)
                        }
                    }
                }
            }
        }

        val btFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }

        context.registerReceiver(btConnectionBroadcastReceiver, btFilter)


        val headsetPlugReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getIntExtra("state", -1)){
                    HEADSET_CONNECTED -> {
                        if (callActive && canAutoSwitchProfile){
                            setMediaOutput(MediaOutputType.WIRED_HEADPHONE, false)
                        }
                    }
                    HEADSET_DISCONNECTED -> {
                        if (callActive && currentMediaOutputType == MediaOutputType.WIRED_HEADPHONE){
                            setMediaOutput(MediaOutputType.PHONE, false)
                        }
                    }
                }
            }
        }

        val headsetFiler = IntentFilter().apply {
            addAction(Intent.ACTION_HEADSET_PLUG)
        }

        context.registerReceiver(headsetPlugReceiver, headsetFiler)
    }

    fun getAvailableMediaOutputs(): ArrayList<MediaOutputModel>{
        val mediaOutputs = arrayListOf<MediaOutputModel>()

        mediaOutputs.add(
            MediaOutputModel(
                name = "Телефон",
                isActive = currentMediaOutputType == MediaOutputType.PHONE,
                type = MediaOutputType.PHONE
            )
        )
        mediaOutputs.add(
            MediaOutputModel(
                name = "Динамик",
                isActive = currentMediaOutputType == MediaOutputType.SPEAKERPHONE,
                type = MediaOutputType.SPEAKERPHONE
            )
        )
        if (isHeadsetOn()){
            mediaOutputs.add(
                MediaOutputModel(
                    name = "Наушники",
                    isActive = currentMediaOutputType == MediaOutputType.WIRED_HEADPHONE,
                    type = MediaOutputType.WIRED_HEADPHONE
                )
            )
        }

        if (bluetoothManager.adapter.isEnabled){
            val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            for (device in devices) {
                if (device.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP || device.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                    mediaOutputs.add(
                        MediaOutputModel(
                            name = "Bluetooth",
                            isActive = currentMediaOutputType == MediaOutputType.BLUETOOTH,
                            type = MediaOutputType.BLUETOOTH
                        )
                    )
                    break
                }
            }
        }
        return mediaOutputs
    }

    fun setMediaOutput(mediaOutput: MediaOutputType, fromUser: Boolean){
        if (fromUser && currentMediaOutputType == MediaOutputType.BLUETOOTH && mediaOutput != MediaOutputType.BLUETOOTH){
            canAutoSwitchProfile = false
        }
        currentMediaOutputType = mediaOutput

        when (currentMediaOutputType){
            MediaOutputType.BLUETOOTH -> {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.startBluetoothSco()
                audioManager.isBluetoothScoOn = true
                audioManager.isSpeakerphoneOn = false
            }
            MediaOutputType.WIRED_HEADPHONE -> {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.stopBluetoothSco()
                audioManager.isBluetoothScoOn = false
                audioManager.isSpeakerphoneOn = false
            }
            MediaOutputType.SPEAKERPHONE -> {
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.stopBluetoothSco()
                audioManager.isBluetoothScoOn = false
                audioManager.isSpeakerphoneOn = true
            }
            MediaOutputType.PHONE -> {
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.stopBluetoothSco()
                audioManager.isBluetoothScoOn = false
                audioManager.isSpeakerphoneOn = false
            }
        }

        selectedMediaOutputSubject.onNext(currentMediaOutputType)
    }

    fun onCallStarted(){
        canAutoSwitchProfile = true
        callActive = true

        wasSpeakerphoneOn = audioManager.isSpeakerphoneOn
        wasMicrophoneMute = audioManager.isMicrophoneMute
        initialAudioMode = audioManager.mode

        setInitialMediaOutput()
    }

    fun onCallEnded(){
        callActive = false

        with(audioManager){
            isSpeakerphoneOn = wasSpeakerphoneOn
            isMicrophoneMute = wasMicrophoneMute
            mode = initialAudioMode
        }
    }

    fun getInitialMediaOutput(): MediaOutputType {
        return when {
            isBTHeadsetOn() -> {
                MediaOutputType.BLUETOOTH
            }
            isHeadsetOn() -> {
                MediaOutputType.WIRED_HEADPHONE
            }
            else -> {
                MediaOutputType.PHONE
            }
        }
    }

    private fun setInitialMediaOutput(){
        currentMediaOutputType = when {
            isBTHeadsetOn() -> {
                MediaOutputType.BLUETOOTH
            }
            isHeadsetOn() -> {
                MediaOutputType.WIRED_HEADPHONE
            }
            else -> {
                MediaOutputType.PHONE
            }
        }

        when (currentMediaOutputType){
            MediaOutputType.BLUETOOTH -> {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.startBluetoothSco()
                audioManager.isBluetoothScoOn = true
                audioManager.isSpeakerphoneOn = false
            }
            MediaOutputType.WIRED_HEADPHONE -> {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.stopBluetoothSco()
                audioManager.isBluetoothScoOn = false
                audioManager.isSpeakerphoneOn = false
            }
            MediaOutputType.SPEAKERPHONE -> {
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.stopBluetoothSco()
                audioManager.isBluetoothScoOn = false
                audioManager.isSpeakerphoneOn = true
            }
            MediaOutputType.PHONE -> {
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.stopBluetoothSco()
                audioManager.isBluetoothScoOn = false
                audioManager.isSpeakerphoneOn = false
            }
        }

        selectedMediaOutputSubject.onNext(currentMediaOutputType)
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

}