package com.custom.rgs_android_dom.ui.location

import android.util.Log
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class RequestLocationRationaleViewModel() : BaseViewModel() {

    fun onSettingsScreenClosed(){
        Log.d("MyLog", "ON SCREEN CLOSED")
        closeController.value = Unit
    }
}