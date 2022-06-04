package com.custom.rgs_android_dom.data.repositories.notifications

import android.os.Bundle
import com.custom.rgs_android_dom.domain.repositories.NotificationsRepository
import com.jakewharton.rxrelay2.BehaviorRelay

class NotificationsRepositoryImpl : NotificationsRepository {

    private val notificationContentSubject = BehaviorRelay.create<Bundle>()

    override fun getNotificationContentSubject(): BehaviorRelay<Bundle> {
        return notificationContentSubject
    }

    override fun newNotification(bundle: Bundle) {
        notificationContentSubject.accept(bundle)
    }

}