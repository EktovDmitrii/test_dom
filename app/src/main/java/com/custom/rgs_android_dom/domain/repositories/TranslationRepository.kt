package com.custom.rgs_android_dom.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single

interface TranslationRepository {

    fun loadAndSaveTranslations(): Completable

    fun getTranslate(key: String): String

}