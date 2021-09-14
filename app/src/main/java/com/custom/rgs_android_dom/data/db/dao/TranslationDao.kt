package com.custom.rgs_android_dom.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.custom.rgs_android_dom.data.db.models.TranslationDBModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TranslationDao {
    @Query("SELECT * FROM ${TranslationDBModel.TABLE_NAME}")
    fun getTranslationsMaybe(): Single<List<TranslationDBModel>>

    @Query("SELECT * FROM ${TranslationDBModel.TABLE_NAME} WHERE `key` = :key")
    fun getTranslationByKey(key: String): Single<TranslationDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(models: List<TranslationDBModel>): Completable

    @Query("DELETE FROM ${TranslationDBModel.TABLE_NAME}")
    fun clear(): Completable
}