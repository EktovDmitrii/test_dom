package com.custom.rgs_android_dom.ui.sos

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.chat.call.CallFragment
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SOSViewModel(
    private val chatInteractor: ChatInteractor,
    private val registrationInteractor: RegistrationInteractor,
    private val clientInteractor: ClientInteractor,
    private val context: Context
) : BaseViewModel() {

    private var isAuthorized = false

    private var requestedScreen = TargetScreen.UNSPECIFIED

    init {
        isAuthorized = registrationInteractor.isAuthorized()

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    isAuthorized = registrationInteractor.isAuthorized()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getClientSavedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    if (isAuthorized) {
                        when (requestedScreen) {
                            TargetScreen.PHONE_CALL -> onFreePhoneCallClick()
                            TargetScreen.CHAT -> { ScreenManager.showScreen(ChatFragment()) }
                            TargetScreen.AUDIO_CALL -> {
                                ScreenManager.showScreen(
                                    CallFragment.newInstance(
                                        CallType.AUDIO_CALL,
                                        chatInteractor.getCurrentConsultant()
                                    )
                                )
                            }

                            TargetScreen.VIDEO_CALL -> {
                                ScreenManager.showScreen(
                                    CallFragment.newInstance(
                                        CallType.VIDEO_CALL,
                                        chatInteractor.getCurrentConsultant()
                                    )
                                )
                            }
                            TargetScreen.UNSPECIFIED -> {}
                        }
                        requestedScreen = TargetScreen.UNSPECIFIED
                    }
                },
                onError = { logException(this, it) }
            ).addTo(dataCompositeDisposable)

    }

    fun onAudioCallClick() {
        requestedScreen = TargetScreen.AUDIO_CALL
        if (isAuthorized) {
            ScreenManager.showScreen(
                CallFragment.newInstance(
                    CallType.AUDIO_CALL,
                    chatInteractor.getCurrentConsultant()
                )
            )
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onVideoCallClick() {
        requestedScreen = TargetScreen.VIDEO_CALL
        if (isAuthorized) {
            ScreenManager.showScreen(
                CallFragment.newInstance(
                    CallType.VIDEO_CALL,
                    chatInteractor.getCurrentConsultant()
                )
            )
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onChatClick() {
        requestedScreen = TargetScreen.CHAT
        if (isAuthorized) {
            ScreenManager.showScreen(ChatFragment())
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onFreePhoneCallClick() {
        val phoneCallIntent = Intent(Intent.ACTION_DIAL)
        phoneCallIntent.data = Uri.parse("tel:8800")
        phoneCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(phoneCallIntent)
    }

}