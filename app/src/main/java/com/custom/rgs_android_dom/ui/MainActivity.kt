package com.custom.rgs_android_dom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.domain.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.splash.SplashFragment
import com.custom.rgs_android_dom.utils.CacheHelper
import com.custom.rgs_android_dom.utils.TranslationHelper
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val translationInteractor: TranslationInteractor by inject()

    companion object{
        private const val LOG = "MAIN"
    }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenManager.init(this, R.id.vgScreensContainer)
        startSplash()
        CacheHelper.init()
        loadTranslation()
    }

    private fun loadTranslation(){
        translationInteractor.loadTranslation()
            .retry(1000)
            .andThen(translationInteractor.getTranslation())
            .flatMapCompletable {
                TranslationHelper.setTranslations(it)
                Completable.complete()
            }
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
        //ScreenManager.showScreen(RegistrationFillClientFragment.newInstance("+7 123 456-77-77"))
        //ScreenManager.showScreen(RegistrationPhoneFragment())
        //ScreenManager.showScreen(DemoRegistrationFlowFragment())
        //ScreenManager.showScreen(MainFragment())
        //ScreenManager.showScreen(EditPersonalDataFragment())
        //ScreenManager.showScreen(PersonalDataFragment())

    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        val topFragment = fragmentList.last { it is BaseFragment<*, *> } as? BaseFragment<*, *>
        if (topFragment != null) {
            topFragment.onClose()
        } else {
            super.onBackPressed()
        }
    }


}