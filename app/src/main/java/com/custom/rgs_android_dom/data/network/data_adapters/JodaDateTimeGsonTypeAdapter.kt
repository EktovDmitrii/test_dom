package com.custom.rgs_android_dom.data.network.data_adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class JodaDateTimeGsonTypeAdapter : TypeAdapter<DateTime>() {

    companion object {
        private const val PATTERN_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssZZ"
    }

    override fun write(out: JsonWriter, value: DateTime) {
        out.value(value.toString(DateTimeFormat.forPattern(PATTERN_DATE_TIME)))
    }

    override fun read(jsonReader: JsonReader): DateTime {
        val str = jsonReader.nextString()
        return DateTime.parse(str, DateTimeFormat.forPattern(PATTERN_DATE_TIME))
    }
}


