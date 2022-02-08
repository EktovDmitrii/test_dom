package com.custom.rgs_android_dom.ui.policies.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.POLICY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AddPolicyViewModel(val policiesInteractor: PoliciesInteractor) : BaseViewModel() {

    private val policyChangedController = MutableLiveData<String>()
    val policyChangedObserver: LiveData<String> = policyChangedController

    init {
        policiesInteractor.getPolicySubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { policyChangedController.value = it }, onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        close()
    }

    fun onNextClick(fragmentId: Int) {
        ScreenManager.showScreenScope(InsurantFragment.newInstance(fragmentId), POLICY)
    }

    fun policyChanged(policy: String) {
        policiesInteractor.policyChanged(policy)
    }

}