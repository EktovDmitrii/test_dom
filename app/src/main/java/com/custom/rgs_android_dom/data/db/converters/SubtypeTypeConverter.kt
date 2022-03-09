package com.custom.rgs_android_dom.data.db.converters

import androidx.room.TypeConverter
import com.custom.rgs_android_dom.data.db.models.chat.SubtypeDbModel
import com.google.gson.Gson

object SubtypeTypeConverter {

    @TypeConverter
    fun fromSubtype(subtype: SubtypeDbModel?): String? {
        return Gson().toJson(subtype)
    }

    @TypeConverter
    fun toSubtype(subtypeStr: String?): SubtypeDbModel?{
        return Gson().fromJson(subtypeStr, SubtypeDbModel::class.java)
    }

}