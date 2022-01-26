package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import io.reactivex.Single

interface PoliciesRepository {

    fun getPolicies() : Single<List<PolicyModel>>

}