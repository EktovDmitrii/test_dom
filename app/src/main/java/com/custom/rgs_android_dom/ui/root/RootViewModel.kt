package com.custom.rgs_android_dom.ui.root

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthState
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallInfoModel
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.Sender
import com.custom.rgs_android_dom.domain.chat.models.WsCallAcceptModel
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputType
import com.custom.rgs_android_dom.domain.chat.models.WsEvent
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.fcm.NotificationsInteractor
import com.custom.rgs_android_dom.domain.fcm.models.NotificationEvent
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.chats.call_request.CallRequestFragment
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.chats.call.CallFragment
import com.custom.rgs_android_dom.ui.chats.ChatsFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.client.order_detail.OrderDetailFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.custom.rgs_android_dom.ui.managers.MediaOutputManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.inject

class RootViewModel(private val registrationInteractor: RegistrationInteractor,
                    private val clientInteractor: ClientInteractor,
                    private val chatInteractor: ChatInteractor,
                    private val notificationsInteractor: NotificationsInteractor,
                    private val mediaOutputManager: MediaOutputManager,
                    private val notificationManager: MSDNotificationManager
) : BaseViewModel() {

    private val navScopesVisibilityController = MutableLiveData<List<Pair<NavigationScope, Boolean>>>()
    val navScopesVisibilityObserver: LiveData<List<Pair<NavigationScope, Boolean>>> = navScopesVisibilityController

    private val isUserAuthorizedController = MutableLiveData<Boolean>()
    val isUserAuthorizedObserver: LiveData<Boolean> = isUserAuthorizedController

    private val unreadPostsController = MutableLiveData<Int>()
    val unreadPostsObserver: LiveData<Int> = unreadPostsController

    private val callInfoController = MutableLiveData<CallInfoModel>()
    val callInfoObserver: LiveData<CallInfoModel> = callInfoController

    private val mediaOutputController = MutableLiveData<MediaOutputType>()
    val mediaOutputObserver: LiveData<MediaOutputType> = mediaOutputController

    private val authContentProviderManager: AuthContentProviderManager by inject()
    private var requestedScreen = TargetScreen.UNSPECIFIED

    init {
        checkSignedAgreement()
        subscribeAuthStateChanges()

        isUserAuthorizedController.value = registrationInteractor.isAuthorized()

        val scopes = listOf(
            Pair(NavigationScope.NAV_PROFILE, registrationInteractor.isAuthorized()),
            Pair(NavigationScope.NAV_LOGIN, !registrationInteractor.isAuthorized())
        )
        navScopesVisibilityController.value = scopes

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val scopes = listOf(
                        Pair(NavigationScope.NAV_PROFILE, true),
                        Pair(NavigationScope.NAV_LOGIN, false)
                    )
                    navScopesVisibilityController.value = scopes
                    isUserAuthorizedController.value = true
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        registrationInteractor.getAuthFlowEndedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    loadCases()
                    when (requestedScreen) {
                        TargetScreen.CHAT -> {
                            ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
                        }
                        TargetScreen.AUDIO_CALL -> {
                            ScreenManager.showScreen(
                                CallFragment.newInstance(
                                    chatInteractor.getMasterOnlineCase().channelId,
                                    CallType.AUDIO_CALL,
                                    chatInteractor.getCurrentConsultant()
                                )
                            )
                        }
                        TargetScreen.VIDEO_CALL -> {
                            ScreenManager.showScreen(
                                CallFragment.newInstance(
                                    chatInteractor.getMasterOnlineCase().channelId,
                                    CallType.VIDEO_CALL,
                                    chatInteractor.getCurrentConsultant()
                                )
                            )
                        }
                        TargetScreen.CHATS -> {
                            ScreenManager.showBottomScreen(ChatsFragment())
                        }
                    }
                    requestedScreen = TargetScreen.UNSPECIFIED
                },
                onError = { logException(this, it) }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getUnreadPostsCountFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    unreadPostsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getWsEventsSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it.event){
                        WsEvent.POSTED -> {
                            loadCases()
                        }
                        WsEvent.CALL_ACCEPT -> {
                            val acceptModel = it as WsCallAcceptModel
                            if (acceptModel.caller == Sender.OPPONENT)
                                showRequestingCall(acceptModel)
                        }
                        WsEvent.CALL_DECLINED -> {
                            notificationManager.cancel(NotificationsInteractor.NOTIFICATION_INCOMING_CALL)
                        }
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    val scopes = listOf(
                        Pair(NavigationScope.NAV_PROFILE, false),
                        Pair(NavigationScope.NAV_LOGIN, true)
                    )
                    navScopesVisibilityController.value = scopes
                    isUserAuthorizedController.value = false

                    if (it == TargetScreen.REGISTRATION) {
                        ScreenManager.resetStackAndShowScreen(RegistrationPhoneFragment())
                    } else {
                        ScreenManager.resetStackAndShowScreen(RootFragment())
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getCallInfoSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    callInfoController.value = it
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        mediaOutputManager.selectedMediaOutputSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    mediaOutputController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        mediaOutputController.value = mediaOutputManager.getInitialMediaOutput()

        notificationsInteractor.getNotificationContentSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    parseNewNotificationContent(it)
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        if (registrationInteractor.isAuthorized()){
            loadCases()
        }
    }

    private fun showRequestingCall(wsModel: WsCallAcceptModel) {
        ScreenManager.showScreen(CallRequestFragment.newInstance(wsModel.callerId, wsModel.callId, wsModel.channelId))
    }

    fun onChatClick() {
        if (registrationInteractor.isAuthorized()) {
            ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
        } else {
            requestedScreen = TargetScreen.CHAT
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onMainClick(){
        ScreenManager.showBottomScreen(MainFragment())
    }

    fun onCatalogueClick(){
        ScreenManager.showBottomScreen(MainCatalogFragment.newInstance())
    }

    fun onProfileClick(){
        ScreenManager.showBottomScreen(ClientFragment())
    }

    fun onLoginClick(){
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
    }

    fun onChatCallClick(){
        if (registrationInteractor.isAuthorized()) {
            ScreenManager.showScreen(
                CallFragment.newInstance(
                    chatInteractor.getMasterOnlineCase().channelId,
                    CallType.AUDIO_CALL,
                    chatInteractor.getCurrentConsultant()
                )
            )
        } else {
            requestedScreen = TargetScreen.AUDIO_CALL
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onChatVideoCallClick(){
        if (registrationInteractor.isAuthorized()) {
            ScreenManager.showScreen(
                CallFragment.newInstance(
                    chatInteractor.getMasterOnlineCase().channelId,
                    CallType.VIDEO_CALL,
                    chatInteractor.getCurrentConsultant()
                )
            )
        } else {
            requestedScreen = TargetScreen.VIDEO_CALL
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onChatsClick(){
        if (registrationInteractor.isAuthorized()) {
            ScreenManager.showBottomScreen(ChatsFragment())
        } else {
            requestedScreen = TargetScreen.CHATS
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onMaximizeCallClick(){
        callInfoController.value?.let { callInfo->
            val callFragment = CallFragment.newInstance(
                callInfo.channelId ?: "",
                callInfo.callType ?: CallType.AUDIO_CALL,
                chatInteractor.getCurrentConsultant()
            )
            ScreenManager.showScreen(callFragment)
        }
    }

    private fun checkSignedAgreement(){
        if (registrationInteractor.isAuthorized()){
            clientInteractor.getClient()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        if (!it.isOpdSigned){
                            ScreenManager.showScreenScope(RegistrationAgreementFragment.newInstance(it.phone), REGISTRATION)
                        }
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }

    private fun subscribeAuthStateChanges(){
        registrationInteractor.getAuthStateSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it){
                        AuthState.AUTH_CLEARED -> {
                            logout()
                        }
                        AuthState.AUTH_SAVED -> {
                            logoutAndReloadClient()
                        }
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun logoutAndReloadClient(){
        registrationInteractor.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen(clientInteractor.loadAndSaveClient())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(dataCompositeDisposable)
    }

    private fun logout() {
        registrationInteractor.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(dataCompositeDisposable)
    }

    private fun loadCases(){
        chatInteractor.loadCases()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    private fun parseNewNotificationContent(content: Bundle){
        if (registrationInteractor.isAuthorized()){
            when (content.getString(NotificationsInteractor.EXTRA_EVENT)){
                NotificationEvent.NEW_MESSAGE,
                NotificationEvent.NEW_WIDGET -> {
                    if (!ScreenManager.containsScreen(CallRequestFragment::class.java.canonicalName)) {
                        chatInteractor.getActiveCallsInAllCases()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onSuccess = {
                                    if (it.isEmpty()){
                                        val channelId = content.getString(NotificationsInteractor.EXTRA_CHANNEL_ID) ?: ""
                                        chatInteractor.getCase(channelId)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeBy(
                                                onSuccess = {
                                                    val chatFragment = ChatFragment.newInstance(it)
                                                    ScreenManager.showBottomScreen(chatFragment)
                                                },
                                                onError = {
                                                    logException(this, it)
                                                }
                                            ).addTo(dataCompositeDisposable)
                                    } else {
                                        val callRequestFragment = CallRequestFragment.newInstance(
                                            callerId = it[0].recipientUserId,
                                            callId = it[0].callId,
                                            channelId = it[0].channelId
                                        )
                                        ScreenManager.showScreen(callRequestFragment)
                                    }
                                },
                                onError = {
                                    logException(this, it)
                                }
                            ).addTo(dataCompositeDisposable)
                    }
                }
                NotificationEvent.CALL_CONSULTANT  -> {
                    val channelId = content.getString(NotificationsInteractor.EXTRA_CHANNEL_ID) ?: ""
                    val callerId = content.getString(NotificationsInteractor.INITIATOR_USER_ID) ?: ""
                    val callId = content.getString(NotificationsInteractor.CALL_ID) ?: ""

                    chatInteractor.getActiveCall(channelId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = {
                                val callRequestFragment = CallRequestFragment.newInstance(
                                    callerId = callerId,
                                    callId = callId,
                                    channelId = channelId
                                )
                                if (!ScreenManager.containsScreen(callRequestFragment)) {
                                    ScreenManager.showScreen(callRequestFragment)
                                }
                            },
                            onError = {
                                logException(this, it)
                            }
                        ).addTo(dataCompositeDisposable)
                }
                NotificationEvent.ORDER_CANCELLED,
                NotificationEvent.ORDER_COMPLETED -> {
                    val orderId = content.getString(NotificationsInteractor.EXTRA_ORDER_ID) ?: ""
                    clientInteractor.getOrder(orderId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = {
                                ScreenManager.showScreen(OrderDetailFragment.newInstance(it))
                            },
                            onError = {
                                logException(this, it)
                            }
                        ).addTo(dataCompositeDisposable)
                }
            }
        }
    }

}