package com.custom.rgs_android_dom.domain.policies.models

import org.joda.time.DateTime
import java.io.Serializable

data class PolicyDialogModel(
    val failureMessage: Failure? = null,
    val showLoader: Boolean = false,
    val bound: BoundPolicyDialogModel? = null,
    val showPrompt: ShowPromptModel? = null
): Serializable

data class BoundPolicyDialogModel(
    val clientProductIds: List<String>,
    val contractId: String,
    val name: String,
    val logo: String,
    val startsAt: DateTime?,
    val endsAt: DateTime?,
    val contractClientBirthDate: String,
    val contractClientFirstName: String,
    val contractClientLastName: String,
    val contractClientMiddleName: String
): Serializable

enum class ShowPromptModel{
     Loading,
     Content,
     Done
}


