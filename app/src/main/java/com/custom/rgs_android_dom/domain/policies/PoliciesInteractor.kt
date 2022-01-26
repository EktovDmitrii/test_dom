package com.custom.rgs_android_dom.domain.policies

import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import io.reactivex.Single

class PoliciesInteractor(val policiesRepository: PoliciesRepository) {

    fun getPolicies(): Single<List<PolicyModel>> {
        return policiesRepository.getPolicies()
    }
}