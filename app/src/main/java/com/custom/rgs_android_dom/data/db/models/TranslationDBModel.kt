package com.custom.rgs_android_dom.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.custom.rgs_android_dom.data.db.models.TranslationDBModel.Companion.TABLE_NAME
import com.custom.rgs_android_dom.data.network.responses.TranslationResponse

@Entity(tableName = TABLE_NAME)
data class TranslationDBModel(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String,

    @ColumnInfo(name = "value")
    val value: String
) {

    companion object {

        const val TABLE_NAME = "Translations"

        fun fromResponse(response: TranslationResponse): TranslationDBModel{
            return TranslationDBModel(key = response.key, value = response.value)
        }

    }
}