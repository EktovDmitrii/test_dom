package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.policies.add.PolicyDialogModel

class PolicyDialogsViewModel(val model: PolicyDialogModel) : BaseViewModel() {

    private val dialogModelController = MutableLiveData<PolicyDialogModel>()
    val dialogModelObserver: LiveData<PolicyDialogModel> = dialogModelController

    init {
        dialogModelController.value = model
    }

    fun onCloseClick() {
        close()
    }

    fun onSavePolicyClick() {
        close()
    }

    fun onCancelClick() {
        close()
    }

    fun onUnderstandClick() {
        close()
    }

    fun onChatClick() {
        close()
        ScreenManager.showScreen(ChatFragment())
    }

    fun onChangeDataClick() {
        close()
    }

}
