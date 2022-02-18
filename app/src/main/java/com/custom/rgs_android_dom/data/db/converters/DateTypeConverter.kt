package com.custom.rgs_android_dom.data.db.converters

import android.util.Log
import androidx.room.TypeConverter
import com.custom.rgs_android_dom.utils.formatTo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime
import java.lang.reflect.Type

object DateTypeConverter {

    @TypeConverter
    fun fromDate(dateTime: DateTime): String {
        return dateTime.formatTo("yyyy-MM-dd'T'HH:mm:ssZZ")
    }

    @TypeConverter
    fun toDateTime(dateTimeStr: String): DateTime {
        return DateTime.parse(dateTimeStr)
    }

}