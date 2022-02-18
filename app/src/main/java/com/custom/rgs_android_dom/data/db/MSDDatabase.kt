package com.custom.rgs_android_dom.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.custom.rgs_android_dom.data.db.converters.CaseConverter
import com.custom.rgs_android_dom.data.db.converters.DateTypeConverter
import com.custom.rgs_android_dom.data.db.converters.SubtypeTypeConverter
import com.custom.rgs_android_dom.data.db.dao.ChatsDao
import com.custom.rgs_android_dom.data.db.models.chat.CaseDbModel

@Database(
    entities = [
        CaseDbModel::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    SubtypeTypeConverter::class,
    CaseConverter::class,
    DateTypeConverter::class
)

abstract class MSDDatabase : RoomDatabase() {

    abstract val chatsDao: ChatsDao

    companion object {
        fun create(context: Context): MSDDatabase {
            return Room.databaseBuilder(context, MSDDatabase::class.java, "msd-database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
