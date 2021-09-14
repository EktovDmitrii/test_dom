package com.custom.rgs_android_dom.data.repositories.translation

import com.custom.rgs_android_dom.data.db.AppDatabase
import com.custom.rgs_android_dom.data.db.models.TranslationDBModel
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.responses.TranslationResponse
import com.custom.rgs_android_dom.domain.repositories.TranslationRepository
import io.reactivex.Completable
import io.reactivex.Single

class TranslationRepositoryImpl(database: AppDatabase, private val api: MSDApi) : TranslationRepository{

    companion object{
        const val PLATFORM = "mobile"
        const val LANG = "ru"
        const val PROJECT = "auto"
    }

    private val translationDao = database.translationDao

    override fun loadAndSaveTranslations(): Completable {
        return getTranslationsFromNetwork()
            .map { it.map { TranslationDBModel.fromResponse(it) } }
            .flatMapCompletable {listTranslation ->
                translationDao.insert(listTranslation) }
    }

    private fun getTranslationsFromNetwork(): Single<List<TranslationResponse>>{
        return api.getTranslations(PLATFORM, LANG, PROJECT)
            .map { it.list }
    }

    override fun getTranslations(): Single<HashMap<String, String>> {
        return translationDao.getTranslationsMaybe()
            .flatMap {
                if(it.isEmpty()){
                    getTranslationsFromNetwork()
                        .map { it.map { TranslationDBModel.fromResponse(it) }}
                } else {
                    Single.just(it)
                }
            }
            .map {
                val hashMap = HashMap<String, String>()
                it.forEach { hashMap[it.key] = it.value }
                hashMap
            }
    }

}