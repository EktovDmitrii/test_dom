package com.custom.rgs_android_dom.data.network.data_adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class JodaDateTimeGsonTypeAdapter : TypeAdapter<DateTime?>() {

    companion object {
        private const val PATTERN_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssZZ"
        private const val PATTERN_DATE_TIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    }

    override fun write(out: JsonWriter, value: DateTime?) {
        out.value(value?.toString(DateTimeFormat.forPattern(PATTERN_DATE_TIME)))
    }

    override fun read(jsonReader: JsonReader): DateTime? {
        if (jsonReader.peek() == JsonToken.NULL){
            jsonReader.nextNull()
            return null
        }
        val str = jsonReader.nextString()
        return try{
            DateTime.parse(str, DateTimeFormat.forPattern(PATTERN_DATE_TIME))
        } catch (e: Exception){
            DateTime.parse(str, DateTimeFormat.forPattern(PATTERN_DATE_TIME_MILLIS))
        }
    }
}


