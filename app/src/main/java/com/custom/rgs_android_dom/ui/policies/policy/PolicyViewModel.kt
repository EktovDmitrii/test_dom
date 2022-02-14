package com.custom.rgs_android_dom.ui.policies.policy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.CacheHelper
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PolicyViewModel(contractId: String, private val policiesInteractor: PoliciesInteractor) : BaseViewModel() {

    private val productController = MutableLiveData<PolicyModel>()
    val productObserver: LiveData<PolicyModel> = productController

    init {
        policiesInteractor.getClientProductSingle(contractId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { logException(this, it) },
                onSuccess = { productController.value = it}
            )
            .addTo(CacheHelper.compositeDisposable)

    }

    fun onBackClick() {
        Log.d("Syrgashev", "onBackClick: ")
        close()
    }

}