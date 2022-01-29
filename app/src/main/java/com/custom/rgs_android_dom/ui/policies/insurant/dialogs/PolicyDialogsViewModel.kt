package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PolicyDialogsViewModel(val policiesInteractor: PoliciesInteractor, model: PolicyDialogModel) : BaseViewModel() {

    private val dialogModelController = MutableLiveData<PolicyDialogModel>()
    val dialogModelObserver: LiveData<PolicyDialogModel> = dialogModelController

    init {
        dialogModelController.value = model

        policiesInteractor.getBindPolicySubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { Log.d("Syrgashev", "$this policiesInteractor.getBindPolicySubject() is called: ") }
            .subscribeBy(
                onNext = {
                    Log.d("Syrgashev", "model: $it")
                    dialogModelController.value = it },
                onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
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
