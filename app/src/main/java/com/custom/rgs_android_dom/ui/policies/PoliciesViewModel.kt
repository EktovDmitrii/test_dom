package com.custom.rgs_android_dom.ui.policies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.policies.add.AddPolicyFragment
import com.custom.rgs_android_dom.utils.CacheHelper.compositeDisposable
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PoliciesViewModel(policyInteractor: PoliciesInteractor) : BaseViewModel() {

    private val policiesController = MutableLiveData<List<PolicyModel>>()
    val policiesObserver: LiveData<List<PolicyModel>> = policiesController

    init {
        policyInteractor.getPolicies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    policiesController.value = it
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(compositeDisposable)

    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onAddClick() {
        ScreenManager.showScreen(AddPolicyFragment())
    }

    fun onBindClick() {
        ScreenManager.showScreen(AddPolicyFragment())
    }

    fun onPolicyClick(it: String) {
        Log.d("Syrgashev", "onPolicyClick: id = $it")
        //ScreenManager.showScreen(PolicyFragment.newInstance(it))
    }
}