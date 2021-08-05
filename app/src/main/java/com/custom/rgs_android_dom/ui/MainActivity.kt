package com.custom.rgs_android_dom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.fill_profile.RegistrationFillProfileFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.ui.splash.SplashFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenManager.init(this, R.id.vgScreensContainer)
        startSplash()
    }

    private fun startSplash(){
        ScreenManager.showScreen(SplashFragment())
        //ScreenManager.showScreen(RegistrationFillProfileFragment.newInstance("+7 123 456-77-77"))
        //ScreenManager.showScreen(RegistrationPhoneFragment())
    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        val topFragment = fragmentList.last { it is BaseFragment<*,*> } as? BaseFragment<*,*>
        if (topFragment != null){
            topFragment.onClose()
        } else {
            super.onBackPressed()
        }
    }
}