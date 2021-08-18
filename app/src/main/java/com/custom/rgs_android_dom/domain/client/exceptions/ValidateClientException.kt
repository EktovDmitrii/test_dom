package com.custom.rgs_android_dom.domain.client

import java.lang.RuntimeException

class ValidateClientException(val field: ProfileField, message: String): RuntimeException(message)

enum class ProfileField{
    NAME,
    SURNAME,
    BIRTHDATE,
    AGENTCODE,
    AGENTPHONE
}