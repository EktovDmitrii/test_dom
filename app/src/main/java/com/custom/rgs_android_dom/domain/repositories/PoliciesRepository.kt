package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.ui.policies.add.PolicyDialogModel
import io.reactivex.Single

interface PoliciesRepository {

    fun getPolicies() : Single<List<PolicyModel>>

    fun findPolicySingle(policy: String): Single<PolicyDialogModel>

}