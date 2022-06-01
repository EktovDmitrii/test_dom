package com.custom.rgs_android_dom.ui.fcm

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.custom.rgs_android_dom.utils.logException
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MSDMessagingService : FirebaseMessagingService() {

    private val clientInteractor: ClientInteractor by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseInstallations.getInstance().id.addOnSuccessListener {deviceId->
            clientInteractor.saveFCMToken(token, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = {
                        logException(this, it)
                    }
                )
        }

    }

    @SuppressLint("WrongThread")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MyLog", "On message received")
        MSDNotificationManager.notify(
            this,
            "Мой_Сервис Дом",
            "У вас новое уведомление",
            ""
        )
    }

    override fun handleIntent(messageIntent: Intent) {
        Log.d("MyLog", "Handle intent")
        MSDNotificationManager.notify(
            this,
            "Мой_Сервис Дом",
            "У вас новое уведомление",
            ""
        )
    }

}