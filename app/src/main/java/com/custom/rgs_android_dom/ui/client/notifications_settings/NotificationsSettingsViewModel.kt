package com.custom.rgs_android_dom.ui.client.notifications_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.NotificationChannelInfo
import com.custom.rgs_android_dom.domain.client.models.NotificationChannelType
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class NotificationsSettingsViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val notificationChannelsController = MutableLiveData<List<NotificationChannelInfo>>()
    val notificationChannelsObserver: LiveData<List<NotificationChannelInfo>> = notificationChannelsController

    init {
        getNotificationChannels()
    }

    fun onSMSNotificationChannelChecked(isChecked: Boolean) {
        updateNotificationChannel(
            NotificationChannelInfo(
                type = NotificationChannelType.SMS,
                enabled = isChecked
            )
        )
    }

    fun onPushNotificationChannelChecked(isChecked: Boolean) {
        updateNotificationChannel(
            NotificationChannelInfo(
                type = NotificationChannelType.PUSH,
                enabled = isChecked
            )
        )
    }

    private fun getNotificationChannels() {
        clientInteractor.loadAndSaveClient().andThen(clientInteractor.getClientModelSingle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.value = LoadingState.LOADING
            }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    notificationChannelsController.value = it.channels
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun updateNotificationChannel(channel: NotificationChannelInfo) {
        clientInteractor.updateNotificationChannel(channel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    notificationChannelsController.value = it.channels
                },
                onError = {
                    getNotificationChannels()
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}