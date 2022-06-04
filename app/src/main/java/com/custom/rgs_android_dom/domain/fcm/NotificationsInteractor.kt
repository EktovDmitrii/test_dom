package com.custom.rgs_android_dom.domain.fcm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.domain.repositories.NotificationsRepository
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.jakewharton.rxrelay2.BehaviorRelay

class NotificationsInteractor(private val context: Context, private val notificationsRepository: NotificationsRepository) {

    companion object {
        val EXTRA_REDIRECT = "redirect"
        val EXTRA_EVENT = "event"
        val EXTRA_CHANNEL_ID = "channelId"
        val EXTRA_ORDER_ID = "orderId"
        val EXTRA_TITLE = "gcm.notification.title"
        val EXTRA_BODY = "gcm.notification.body"
        val EXTRA_PLATFORM = "platform"
        val INITIATOR_USER_ID = "initiatorUserId"
        val CALL_ID = "callId"
    }

    fun getNotificationContentSubject(): BehaviorRelay<Bundle>{
        return notificationsRepository.getNotificationContentSubject()
    }

    fun parseAndShowNotification(intent: Intent){
        intent.extras?.let { extras->
            val platform = extras.getString(EXTRA_PLATFORM, "")

            if (platform == BuildConfig.BUSINESS_LINE){
                val titleKey = extras.getString(EXTRA_TITLE) ?: ""
                val messageKey = extras.getString(EXTRA_BODY) ?: ""


                MSDNotificationManager.notify(
                    context,
                    TranslationInteractor.getTranslation(titleKey),
                    TranslationInteractor.getTranslation(messageKey),
                    extras
                )
            }
        }

    }

    fun newNotification(intent: Intent){
        intent.extras?.let{
            notificationsRepository.newNotification(it)
        }
    }

}