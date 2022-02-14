package com.custom.rgs_android_dom.ui.policies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.POLICY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.policies.add.AddPolicyFragment
import com.custom.rgs_android_dom.ui.policies.policy.PolicyFragment
import com.custom.rgs_android_dom.utils.CacheHelper.compositeDisposable
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PoliciesViewModel(private val policiesInteractor: PoliciesInteractor, clientInteractor: ClientInteractor) :
    BaseViewModel() {

    private val policiesController = MutableLiveData<List<PolicyShortModel>>()
    val policiesObserver: LiveData<List<PolicyShortModel>> = policiesController

    private val promptSaveController = MutableLiveData<Boolean>()
    val promptSaveObserver: LiveData<Boolean> = promptSaveController

    private var isPersonalDataFilled = false

    init {
        getPolicies()

        clientInteractor.getClientModelSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { logException(this, it) },
                onSuccess = {
                    if (it.birthDate != null &&
                        it.firstName.isNotEmpty() &&
                        it.middleName != null &&
                        it.lastName.isNotEmpty()
                    ) {
                        isPersonalDataFilled = true
                    }
                }
            )
            .addTo(compositeDisposable)

        policiesInteractor.getPromptSaveSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { logException(this, it) },
                onNext = {
                    getPolicies()
                    if (!isPersonalDataFilled) {
                        promptSaveController.value = it
                    }
                }
            )
            .addTo(compositeDisposable)

        clientInteractor.getClientUpdatedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { logException(this, it) },
                onNext = { getPolicies() }
            )
            .addTo(compositeDisposable)

    }

    fun onBackClick() {
        close()
    }

    fun onAddClick(fragmentId: Int) {
        ScreenManager.showScreenScope(AddPolicyFragment.newInstance(fragmentId), POLICY)
    }

    fun onBindClick(fragmentId: Int) {
        ScreenManager.showScreenScope(AddPolicyFragment.newInstance(fragmentId), POLICY)
    }

    fun onPolicyClick(contractId: String) {
        ScreenManager.showScreen(PolicyFragment.newInstance(contractId))
    }

    private fun getPolicies() {
        policiesInteractor.getPoliciesSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { policiesController.value = it },
                onError = { logException(this, it) }
            )
            .addTo(compositeDisposable)
    }
}