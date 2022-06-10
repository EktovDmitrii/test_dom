package com.custom.rgs_android_dom.ui.chats.call_request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.fcm.NotificationsInteractor
import com.custom.rgs_android_dom.domain.fcm.NotificationsInteractor.Companion.NOTIFICATION_INCOMING_CALL
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.chats.call.CallFragment
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CallRequestViewModel(
    private val callerId: String,
    private val callId: String,
    private val channelId: String,
    private val chatInteractor: ChatInteractor,
    private val notificationManager: MSDNotificationManager
) : BaseViewModel() {

    private val isClosableController = MutableLiveData(false)
    val isClosableObserver: LiveData<Boolean> = isClosableController

    private val callEndedController = MutableLiveData<Unit>()
    val callEndedObserver: LiveData<Unit> = callEndedController

    private val consultantController = MutableLiveData<ChannelMemberModel>()
    val consultantObserver: LiveData<ChannelMemberModel> = consultantController

    private var activeCases: List<CaseModel> = emptyList()

    init {

        notificationManager.cancel(NOTIFICATION_INCOMING_CALL)

        chatInteractor.getCurrentConsultant()?.let { consultant ->
            consultantController.value = consultant
        } ?: getConsultantInfoFromRemote()

        chatInteractor.getCasesFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    activeCases = it.activeCases
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        chatInteractor.getWsEventsSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it.event){
                        WsEvent.CALL_DECLINED -> {
                            isClosableController.value = true
                            callEndedController.value = Unit
                        }
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun getConsultantInfoFromRemote() {
        chatInteractor.getMembersFromRemote(chatInteractor.getMasterOnlineCase().channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    it.firstOrNull { it.userId == callerId }?.let { consultant ->
                        consultantController.value = consultant
                    }
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun navigateCall() {
        notificationManager.cancel(NOTIFICATION_INCOMING_CALL)

        isClosableController.value = true
        closeController.value = Unit

        val callFragment = CallFragment.newInstance(
            channelId,
            CallType.AUDIO_CALL,
            chatInteractor.getCurrentConsultant(),
            callId
        )
        ScreenManager.showScreen(callFragment)
    }

    fun declineCall() {
        notificationManager.cancel(NOTIFICATION_INCOMING_CALL)

        isClosableController.value = true

        chatInteractor.declineCall(
            channelId = channelId,
            callId = callId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                    closeController.value = Unit
                }
            ).addTo(dataCompositeDisposable)
    }

    fun navigateChat() {
        notificationManager.cancel(NOTIFICATION_INCOMING_CALL)

        isClosableController.value = true

        chatInteractor.declineCall(
            channelId = channelId,
            callId = callId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    closeController.value = Unit

                    activeCases.firstOrNull { it.channelId == channelId }?.let {
                        ScreenManager.showBottomScreen(ChatFragment.newInstance(it))
                    } ?: ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                }
            ).addTo(dataCompositeDisposable)
    }

}