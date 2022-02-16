package com.custom.rgs_android_dom.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.joda.time.DateTime

object DateTypeConverter {

    @TypeConverter
    fun fromDate(dateTime: DateTime): String {
        return Gson().toJson(dateTime)
    }

    @TypeConverter
    fun toDateTime(dateTimeStr: String): DateTime {
        return Gson().fromJson(dateTimeStr, DateTime::class.java)
    }

}