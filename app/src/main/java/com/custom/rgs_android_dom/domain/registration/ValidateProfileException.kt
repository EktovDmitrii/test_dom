package com.custom.rgs_android_dom.domain.registration

class ValidateProfileException(val field: ProfileField, message: String): Exception(message)

enum class ProfileField{
    NAME,
    SURNAME,
    BIRTHDATE,
    AGENTCODE,
    AGENTPHONE
}