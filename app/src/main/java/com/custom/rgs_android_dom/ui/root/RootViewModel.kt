package com.custom.rgs_android_dom.ui.root

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthState
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.chat.call.CallFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.views.NavigationScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.inject

class RootViewModel(private val registrationInteractor: RegistrationInteractor,
                    private val clientInteractor: ClientInteractor,
                    private val chatInteractor: ChatInteractor
) : BaseViewModel() {

    private val navScopesVisibilityController = MutableLiveData<List<Pair<NavigationScope, Boolean>>>()
    val navScopesVisibilityObserver: LiveData<List<Pair<NavigationScope, Boolean>>> = navScopesVisibilityController

    private val navScopeEnabledController = MutableLiveData<Pair<NavigationScope, Boolean>>()
    val navScopeEnabledObserver: LiveData<Pair<NavigationScope, Boolean>> = navScopeEnabledController

    private val isUserAuthorizedController = MutableLiveData<Boolean>()
    val isUserAuthorizedObserver: LiveData<Boolean> = isUserAuthorizedController

    private val unreadPostsController = MutableLiveData<Int>()
    val unreadPostsObserver: LiveData<Int> = unreadPostsController

    private val authContentProviderManager: AuthContentProviderManager by inject()
    private val logoutCompositeDisposable = CompositeDisposable()
    private var requestedScreen = TargetScreen.UNSPECIFIED

    init {
        checkSignedAgreement()
        subscribeAuthStateChanges()

        unreadPostsController.value = 22

        isUserAuthorizedController.value = registrationInteractor.isAuthorized()

        val scopes = listOf(
            Pair(NavigationScope.NAV_PROFILE, registrationInteractor.isAuthorized()),
            Pair(NavigationScope.NAV_LOGIN, !registrationInteractor.isAuthorized())
        )
        navScopesVisibilityController.value = scopes

        navScopeEnabledController.value = Pair(NavigationScope.NAV_CHAT, registrationInteractor.isAuthorized())

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
                    navScopeEnabledController.value = Pair(NavigationScope.NAV_CHAT, true)

                    isUserAuthorizedController.value = true
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
                    when (requestedScreen) {
                        TargetScreen.CHAT -> {
                            ScreenManager.showBottomScreen(ChatFragment())
                        }
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
                    }
                    requestedScreen = TargetScreen.UNSPECIFIED
                },
                onError = { logException(this, it) }
            ).addTo(dataCompositeDisposable)
    }

    fun subscribeLogout() {
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
                    navScopeEnabledController.value = Pair(NavigationScope.NAV_CHAT, false)

                    isUserAuthorizedController.value = false

                },
                onError = {
                    logException(this, it)
                }
            ).addTo(logoutCompositeDisposable)
    }


    fun unsubscribeLogout() {
        //logoutCompositeDisposable.clear()
    }

    fun onChatClick() {
        if (registrationInteractor.isAuthorized()) {
            ScreenManager.showBottomScreen(ChatFragment())
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
                    CallType.VIDEO_CALL,
                    chatInteractor.getCurrentConsultant()
                )
            )
        } else {
            requestedScreen = TargetScreen.VIDEO_CALL
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
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
                            ScreenManager.showScreenScope(RegistrationAgreementFragment.newInstance(it.phone, true), REGISTRATION)
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

}