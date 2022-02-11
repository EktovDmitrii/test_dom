package com.custom.rgs_android_dom.ui.policies.insurant.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.ShowPromptModel
import com.custom.rgs_android_dom.ui.navigation.POLICY
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

        policiesInteractor.getPolicyDialogSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { dialogModelController.value = it },
                onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
    }

    fun onCloseClick() {
        close()
    }

    fun onSaveClick() {
        policiesInteractor.savePersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { dialogModelController.value = PolicyDialogModel(showPrompt = ShowPromptModel.Done) },
                onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
    }

    fun onCancelClick() {
        policiesInteractor.promptSavePersonalData(false)
        close()
    }

    fun onSuccessClick() {
        policiesInteractor.promptSavePersonalData(false)
        close()
    }

    fun onUnderstandClick(fragmentId: Int) {
        policiesInteractor.promptSavePersonalData(true)
        ScreenManager.closeScreenToId(POLICY, fragmentId)
    }

    fun onChatClick() {
        close()
        ScreenManager.showBottomScreen(ChatFragment())
    }

    fun onChangeDataClick() {
        close()
    }

}
