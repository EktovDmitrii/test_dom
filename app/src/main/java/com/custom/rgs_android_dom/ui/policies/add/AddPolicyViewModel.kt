package com.custom.rgs_android_dom.ui.policies.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AddPolicyViewModel(val policiesInteractor: PoliciesInteractor) : BaseViewModel() {

    private val policyChangedController = MutableLiveData<String>()
    val policyChangedObserver: LiveData<String> = policyChangedController

    /*private val policyController = MutableLiveData<PolicyDialogModel>()
    val policyObserver: LiveData<PolicyDialogModel> = policyController*/

    init {
        policiesInteractor.getInsurantViewStateSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { policyChangedController.value = it.policy }, onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)

        /* policiesInteractor.findPolicySingle()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeBy(onSuccess = { policyController.value = it }, onError = { logException(this, it) })
             .addTo(dataCompositeDisposable)*/
    }

    fun onBackClick() {
        close()
    }

    fun onNextClick() {
        //ScreenManager.showScreen(InsurantFragment())
        policiesInteractor.findPolicySingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {

            }, onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
    }

    fun policyChanged(policy: String) {
        policiesInteractor.policyChanged(policy)
    }

}