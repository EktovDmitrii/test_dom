package com.custom.rgs_android_dom.ui

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.domain.web_socket.WebSocketInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.managers.AuthContentProviderManager
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
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val translationInteractor: TranslationInteractor by inject()
    private val webSocketInteractor: WebSocketInteractor by inject()

    companion object{
        private const val LOG = "MAIN"
    }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenManager.init(this, R.id.vgScreensContainer)
        if (savedInstanceState == null){
            startSplash()
            CacheHelper.init()
            loadTranslation()
            webSocketInteractor.connect()
        }

        val isAuthorized = AuthContentProviderManager.isAuthorized(this)
        val accessToken = AuthContentProviderManager.getAccessToken(this)
        Log.d("MyLog", "IS AUTHORIZED " + isAuthorized + " ACCESS TOKEN " + accessToken)

    }

   /* override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            supportFragmentManager.fragments.lastOrNull { it is DispatchTouchEventListener }?.let {
                (it as DispatchTouchEventListener).dispatchTouchEvent(event)
            }
        }
        return super.dispatchTouchEvent(event)
    }*/

    private fun loadTranslation(){
        translationInteractor.loadTranslation()
            .retryWhen{throwables -> throwables.delay(5, TimeUnit.SECONDS)}
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

    private fun startSplash() {
        ScreenManager.showScreen(SplashFragment())
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

    interface DispatchTouchEventListener{
        fun dispatchTouchEvent(event: MotionEvent)
    }


}