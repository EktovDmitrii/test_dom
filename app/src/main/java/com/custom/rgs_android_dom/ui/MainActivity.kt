package com.custom.rgs_android_dom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.fill_client.RegistrationFillClientFragment
import com.custom.rgs_android_dom.ui.splash.SplashFragment
import com.custom.rgs_android_dom.utils.CashHelper

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenManager.init(this, R.id.vgScreensContainer)
        startSplash()
        CashHelper.init()
    }

    override fun onDestroy() {
        CashHelper.destroy()
        super.onDestroy()
    }

    private fun startSplash() {
        //ScreenManager.showScreen(SplashFragment())
        ScreenManager.showScreen(RegistrationFillClientFragment.newInstance("+7 123 456-77-77"))
        //ScreenManager.showScreen(RegistrationPhoneFragment())
        //ScreenManager.showScreen(DemoRegistrationFlowFragment())
        //ScreenManager.showScreen(MainFragment())
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