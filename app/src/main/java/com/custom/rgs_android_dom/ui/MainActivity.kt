package com.custom.rgs_android_dom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.demo.DemoFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.vgScreensContainer, DemoFragment())
        transaction.commit()
    }
}