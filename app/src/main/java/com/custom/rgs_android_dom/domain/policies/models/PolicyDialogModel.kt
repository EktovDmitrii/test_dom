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
    val startsAt: DateTime?,
    val endsAt: DateTime?
)

enum class ShowPromptModel{
     Loading,
     Content,
     Done
}


