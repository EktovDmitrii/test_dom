package com.custom.rgs_android_dom.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.custom.rgs_android_dom.data.db.dao.TranslationDao
import com.custom.rgs_android_dom.data.db.models.TranslationDBModel



    @Database(
        entities = [
            TranslationDBModel::class
        ],
        version = 1,
        exportSchema = false
    )

    abstract class AppDatabase : RoomDatabase() {

        abstract val translationDao: TranslationDao

        companion object {

            const val DATABASE_NAME = "my-service-dom"

            fun create(context: Context): AppDatabase {
                return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
