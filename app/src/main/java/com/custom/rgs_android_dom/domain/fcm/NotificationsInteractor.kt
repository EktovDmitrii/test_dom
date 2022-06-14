package com.custom.rgs_android_dom.domain.fcm

import android.content.Intent
import android.os.Bundle
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.domain.fcm.models.NotificationEvent
import com.custom.rgs_android_dom.domain.repositories.NotificationsRepository
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.jakewharton.rxrelay2.BehaviorRelay

class NotificationsInteractor(
    private val notificationsRepository: NotificationsRepository,
    private val notificationManager: MSDNotificationManager
) {

    companion object {
        const val EXTRA_REDIRECT = "redirect"
        const val EXTRA_EVENT = "event"
        const val EXTRA_CHANNEL_ID = "channelId"
        const val EXTRA_ORDER_ID = "orderId"
        const val EXTRA_TITLE = "gcm.notification.title"
        const val EXTRA_BODY = "gcm.notification.body"
        const val EXTRA_PLATFORM = "platform"
        const val INITIATOR_USER_ID = "initiatorUserId"
        const val CALL_ID = "callId"
        const val EXTRA_NOTIFICATION_ID = "notificationId"
        const val NOTIFICATION_INCOMING_CALL = 7711
    }

    fun getNotificationContentSubject(): BehaviorRelay<Bundle> {
        return notificationsRepository.getNotificationContentSubject()
    }

    fun parseAndShowNotification(intent: Intent) {
        intent.extras?.let { extras->
            val platform = extras.getString(EXTRA_PLATFORM, "")

            if (platform == BuildConfig.BUSINESS_LINE){
                val titleKey = extras.getString(EXTRA_TITLE) ?: ""
                val messageKey = extras.getString(EXTRA_BODY) ?: ""

                val notificationId = when (extras.getString(EXTRA_EVENT)) {
                    NotificationEvent.NEW_WIDGET,
                    NotificationEvent.NEW_MESSAGE -> {
                        extras.getString(EXTRA_CHANNEL_ID).hashCode()
                    }
                    NotificationEvent.CALL_CONSULTANT -> {
                        NOTIFICATION_INCOMING_CALL
                    }
                    NotificationEvent.ORDER_COMPLETED,
                    NotificationEvent.ORDER_CANCELLED -> {
                        extras.getString(EXTRA_ORDER_ID).hashCode()
                    }
                    else -> {
                        -1
                    }
                }
                extras.putInt(EXTRA_NOTIFICATION_ID, notificationId)

                notificationManager.notify(
                    TranslationInteractor.getTranslation(titleKey),
                    TranslationInteractor.getTranslation(messageKey),
                    notificationId,
                    extras,
                    extras.getString(EXTRA_EVENT)
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