package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewState
import io.reactivex.Observable
import io.reactivex.Single

interface PoliciesRepository {

    fun onPolicyChange(policy: String)

    fun bindPolicy(insurantViewState: InsurantViewState): Single<Any>

    fun getPolicyDialogSubject(): Observable<PolicyDialogModel>

    fun newDialog(policyDialogModel: PolicyDialogModel)

    fun promptSavePersonalData(save: Boolean)

    fun getPromptSaveSubject(): Observable<Boolean>

    fun getPoliciesSingle(): Single<List<PolicyShortModel>>

    fun getPolicySingle(contractId: String): Single<PolicyModel>

    fun getPolicyDialogModel(): PolicyDialogModel?

}