package com.custom.rgs_android_dom.domain.fcm

import android.annotation.SuppressLint
import android.content.Intent
import com.custom.rgs_android_dom.domain.client.ClientInteractor
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
    private val notificationsInteractor: NotificationsInteractor by inject()

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
        notificationsInteractor.parseAndShowNotification(remoteMessage.toIntent())
    }

    override fun handleIntent(messageIntent: Intent) {
        notificationsInteractor.parseAndShowNotification(messageIntent)
    }

}