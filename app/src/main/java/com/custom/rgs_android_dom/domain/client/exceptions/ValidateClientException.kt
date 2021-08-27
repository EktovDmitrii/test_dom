package com.custom.rgs_android_dom.domain.client.exceptions

import java.lang.RuntimeException

class ValidateClientException(val field: ClientField, val errorMessage: String): RuntimeException(errorMessage)

enum class ClientField{
    FIRSTNAME,
    LASTNAME,
    MIDDLENAME,
    BIRTHDATE,
    AGENTCODE,
    AGENTPHONE,
    DOC_SERIAL,
    DOC_NUMBER,
    PHONE,
    SECOND_PHONE,
    EMAIL
}