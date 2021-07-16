package com.custom.rgs_android_dom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.demo.DemoFragment
import com.custom.rgs_android_dom.ui.navigation.NavigationMenu
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.ScreenScope

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenManager.init(this, R.id.vgScreensContainer)
        demoScreen()
    }

    private fun demoScreen(){
        ScreenManager.setMenu(NavigationMenu.HOME)
    }


    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        val topFragment = fragmentList.last { it is BaseFragment<*,*> } as? BaseFragment<*,*>
        if (topFragment != null){
            ScreenManager.back(topFragment.getNavigateId())
        } else {
            super.onBackPressed()
        }

    }
}