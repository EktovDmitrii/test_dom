package com.custom.rgs_android_dom.domain.registration

import java.lang.RuntimeException

class ValidateProfileException(val field: ProfileField, message: String): RuntimeException(message)

enum class ProfileField{
    NAME,
    SURNAME,
    BIRTHDATE,
    AGENTCODE,
    AGENTPHONE
}