package com.custom.rgs_android_dom.domain

import com.custom.rgs_android_dom.domain.repositories.TranslationRepository
import io.reactivex.Completable
import io.reactivex.Single

class TranslationInteractor(val translationRepository: TranslationRepository) {

    fun loadTranslation(): Completable{
        return translationRepository.loadAndSaveTranslations()
    }

    fun getTranslation(): Single<HashMap<String, String>> {
        return translationRepository.getTranslations()
    }

}