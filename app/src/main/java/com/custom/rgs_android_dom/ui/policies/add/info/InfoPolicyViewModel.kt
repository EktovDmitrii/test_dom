package com.custom.rgs_android_dom.ui.policies.add.info

import android.util.Log
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class InfoPolicyViewModel() : BaseViewModel() {

    fun onConfirmClick() {
        Log.d("Syrgashev", "onConfirmClick: ")
        close()
    }

    fun onCloseClick() {
        Log.d("Syrgashev", "onCloseClick: ")
        close()
    }

}
