package com.custom.rgs_android_dom.domain.translations

import com.custom.rgs_android_dom.data.repositories.translation.TranslationRepositoryImpl
import com.custom.rgs_android_dom.domain.error.model.MSDErrorModel
import com.custom.rgs_android_dom.domain.repositories.TranslationRepository
import io.reactivex.Completable
import io.reactivex.Single

class TranslationInteractor(val translationRepository: TranslationRepository) {

    companion object{
        fun getTranslation(key: String): String {
            return TranslationRepositoryImpl.getTranslate(key)
        }

        fun getErrorTranslation(error: MSDErrorModel): String {
            var message = getTranslation(error.messageKey)
            if (message == error.messageKey){
                message = error.defaultMessage
            }
            return message
        }
    }

    fun loadTranslation(): Completable{
        return translationRepository.loadAndSaveTranslations()
    }

    fun getTranslation(key: String): String {
        return translationRepository.getTranslate(key)
    }

}