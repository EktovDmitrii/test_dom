package com.custom.rgs_android_dom.domain.repositories

import android.os.Bundle
import com.jakewharton.rxrelay2.BehaviorRelay

interface NotificationsRepository {

    fun getNotificationContentSubject(): BehaviorRelay<Bundle>

    fun newNotification(bundle: Bundle)

}