package com.custom.rgs_android_dom.ui.fcm

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ProcessLifecycleOwner
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.utils.logException
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
        clientInteractor.saveFCMToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {

                },
                onError = {
                    logException(this, it)
                }
            )
    }

    @SuppressLint("WrongThread")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //ProcessLifecycleOwner.get().lifecycle.currentState
    }

    override fun handleIntent(messageIntent: Intent) {

    }

}