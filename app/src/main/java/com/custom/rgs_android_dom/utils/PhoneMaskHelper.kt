package com.custom.rgs_android_dom.utils

object PhoneMaskHelper {

    fun getMaskForPhone(phone: String): String {
        when {
            phone.startsWith("994") -> {
                return "+### ###-###-###"
            }
            phone.startsWith("375") -> {
                return "+### ###-###-###"
            }
            phone.startsWith("374") -> {
                return "+### ###-###-##"
            }
            phone.startsWith("7") -> {
                return "+# ### ###-##-##"
            }
            phone.startsWith("996") -> {
                return "+### ###-###-###"
            }
            phone.startsWith("373") -> {
                return "+### ###-###-##"
            }
            phone.startsWith("992") -> {
                return "+### ###-###-###"
            }
            phone.startsWith("998") -> {
                return "+### ###-###-##"
            }
            phone.startsWith("380") -> {
                return "+### ## ###-##-##"
            }
            else -> {
                return "+# ### ###-##-##"
            }
        }
    }
}