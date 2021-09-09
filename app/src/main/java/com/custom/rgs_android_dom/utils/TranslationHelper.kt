package com.custom.rgs_android_dom.utils

object TranslationHelper {

    private var cashTranslation = HashMap<String, String>()

    fun getTranslation(key: String): String {
        return cashTranslation[key] ?: key
    }

    fun getTranslationOrNull(key: String): String? {
        return cashTranslation[key]
    }

    fun setTranslations(translations: HashMap<String,String>){
        cashTranslation = translations
    }

}