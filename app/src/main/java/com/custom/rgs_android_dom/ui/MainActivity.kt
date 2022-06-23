package com.custom.rgs_android_dom.ui

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.fcm.NotificationsInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager.Companion.ACTION_NOTIFICATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.splash.SplashFragment
import com.custom.rgs_android_dom.utils.CacheHelper
import com.custom.rgs_android_dom.utils.logException
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val translationInteractor: TranslationInteractor by inject()
    private val chatInteractor: ChatInteractor by inject()
    private val notificationsInteractor: NotificationsInteractor by inject()
    private val registrationInteractor: RegistrationInteractor by inject()
    private val clientInteractor: ClientInteractor by inject()

    companion object{
        private const val LOG = "MAIN"
    }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            CacheHelper.init()
            loadTranslation()
            chatInteractor.connectToWebSocket()
        }

        registrationInteractor.getAuthFlowEndedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    obtainAndSaveFCMToken()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(disposable)

        ScreenManager.init(this, R.id.vgScreensContainer)
        ScreenManager.resetStackAndShowScreen(SplashFragment())

        if (intent != null && intent.action == ACTION_NOTIFICATION){
            notificationsInteractor.newNotification(intent)
        }
    }

    private fun loadTranslation(){
        translationInteractor.loadTranslation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {

                },
                onError = {
                    logException(this, it)
                }
            ).addTo(disposable)
    }

    override fun onDestroy() {
        CacheHelper.destroy()
        disposable.dispose()
        super.onDestroy()
    }



    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        val topFragment = fragmentList.last()
        if (topFragment != null) {
            when(topFragment){
                is BaseFragment<*,*> -> {
                    topFragment.onClose()
                }
                is BaseBottomSheetFragment<*, *> -> {
                    topFragment.onClose()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == ACTION_NOTIFICATION){
            notificationsInteractor.newNotification(intent)
        }
    }

    interface DispatchTouchEventListener{
        fun dispatchTouchEvent(event: MotionEvent)
    }

    private fun obtainAndSaveFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            if (task.isSuccessful){
                FirebaseInstallations.getInstance().id.addOnSuccessListener { deviceId->
                    clientInteractor.saveFCMToken(task.result, deviceId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onError = {
                                logException(this, it)
                            }
                        ).addTo(disposable)
                }
            } else {
                task.exception?.let {
                    logException(this, it)
                }
            }
        }
    }

}