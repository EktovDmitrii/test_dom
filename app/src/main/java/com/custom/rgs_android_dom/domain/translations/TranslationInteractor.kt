package com.custom.rgs_android_dom.domain.translations

import com.custom.rgs_android_dom.data.repositories.translation.TranslationRepositoryImpl
import com.custom.rgs_android_dom.domain.repositories.TranslationRepository
import io.reactivex.Completable
import io.reactivex.Single

class TranslationInteractor(val translationRepository: TranslationRepository) {

    companion object{
        fun getTranslation(key: String): String {
            return TranslationRepositoryImpl.getTranslate(key)
        }
    }

    fun loadTranslation(): Completable{
        return translationRepository.loadAndSaveTranslations()
    }

    fun getTranslation(key: String): String {
        return translationRepository.getTranslate(key)
    }

}