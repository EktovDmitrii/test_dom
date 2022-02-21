package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.domain.policies.models.Failure

object PoliciesMapper {

    private const val MESSAGE_NOT_FOUND = "no contracts found"
    private const val MESSAGE_BOUND_TO_YOUR_PROFILE = "contract is already linked to the current client"
    private const val MESSAGE_BOUND_TO_ANOTHER_PROFILE = "contract is already linked to another client"
    private const val MESSAGE_DATA_NOT_MATCH = "no matched clients for contract"
    private const val MESSAGE_EXPIRED = "contract is expired"


    fun responseErrorToFailure(message: String?): Failure {
        return when (message) {
            MESSAGE_NOT_FOUND -> Failure.NOT_FOUND
            MESSAGE_BOUND_TO_YOUR_PROFILE -> Failure.BOUND_TO_YOUR_PROFILE
            MESSAGE_BOUND_TO_ANOTHER_PROFILE -> Failure.BOUND_TO_ANOTHER_PROFILE
            MESSAGE_DATA_NOT_MATCH -> Failure.DATA_NOT_MATCH
            MESSAGE_EXPIRED -> Failure.EXPIRED
            else -> throw IllegalArgumentException("Unknown error message!")
        }
    }

}