package com.custom.rgs_android_dom.ui.policies.add

import java.io.Serializable

sealed class PolicyDialogModel: Serializable {
    object FindPolicySuccess: PolicyDialogModel()
    object Loading: PolicyDialogModel()
    data class FindPolicyFailure(val message: String): PolicyDialogModel()
}

