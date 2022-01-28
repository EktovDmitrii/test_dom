package com.custom.rgs_android_dom.domain.policies.models

enum class Failure {
    NOT_FOUND,
    BOUND_TO_YOUR_PROFILE,
    BOUND_TO_ANOTHER_PROFILE,
    DATA_NOT_MATCH,
    EXPIRED,
    YET_NOT_DUE
}