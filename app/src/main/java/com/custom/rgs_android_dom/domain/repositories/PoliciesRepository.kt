package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.data.network.requests.BindPolicyRequest
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewState
import io.reactivex.Observable
import io.reactivex.Single

interface PoliciesRepository {

    fun onInsurantDataChange(data: InsurantViewState)

    fun onPolicyChange(policy: String)

    fun bindPolicy(): Single<Any>

    fun getPolicyDialogSubject(): Observable<PolicyDialogModel>

    fun newDialog(policyDialogModel: PolicyDialogModel)

    fun promptSavePersonalData(save: Boolean)

    fun getPromptSaveSubject(): Observable<Boolean>

    fun getRequest(): BindPolicyRequest

    fun getPoliciesSingle(): Single<List<PolicyShortModel>>

    fun getPolicySingle(contractId: String): Single<PolicyModel>

    fun restoreViewState(viewState: InsurantViewState)
}