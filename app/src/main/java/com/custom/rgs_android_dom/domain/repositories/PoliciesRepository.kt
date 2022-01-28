package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface PoliciesRepository {

    fun getPolicies(): Single<List<PolicyModel>>

    fun findPolicySingle(policy: String): Single<PolicyDialogModel>

    fun bindPolicy()

    fun getBindPolicySubject(): PublishSubject<PolicyDialogModel>

}