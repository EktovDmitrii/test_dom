package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.utils.toTimeZone
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

class JodaLocalDateTimeGsonTypeAdapter : TypeAdapter<LocalDateTime>() {

    companion object {
        private const val PATTERN_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssZZ"
        private const val PATTERN_DATE = "yyyy-MM-dd"
    }

    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(value.toString(DateTimeFormat.forPattern(PATTERN_DATE_TIME)))
    }

    override fun read(jsonReader: JsonReader): LocalDateTime? {
        val str = jsonReader.nextString()
        if (str.isNullOrBlank()){
            return null
        }
        return try {
            LocalDateTime.parse(str.toTimeZone(PATTERN_DATE_TIME), DateTimeFormat.forPattern(PATTERN_DATE_TIME))
        } catch (e: Exception) {
            LocalDate.parse(str, DateTimeFormat.forPattern(PATTERN_DATE)).toLocalDateTime(LocalTime.MIDNIGHT)
        }
    }
}


