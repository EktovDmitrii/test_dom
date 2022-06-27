package com.custom.rgs_android_dom.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.fcm.NotificationsInteractor
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager.Companion.ACTION_NOTIFICATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.splash.SplashFragment
import com.custom.rgs_android_dom.utils.CacheHelper
import com.custom.rgs_android_dom.utils.logException
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
            Log.d("MyLog", "Top fragment not null " + fragmentList.size)
            when(topFragment){
                is BaseFragment<*,*> -> {
                    topFragment.onClose()
                }
                is BaseBottomSheetFragment<*, *> -> {
                    topFragment.onClose()
                }
            }
        } else {
            Log.d("MyLog", "Gonna back press")
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
}
