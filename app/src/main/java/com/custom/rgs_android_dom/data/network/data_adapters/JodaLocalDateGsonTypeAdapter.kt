package com.custom.rgs_android_dom.data.network.data_adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class JodaLocalDateGsonTypeAdapter : TypeAdapter<LocalDate>() {

    companion object {
        private const val PATTERN = "yyyy-MM-dd"
    }

    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(value.toString(DateTimeFormat.forPattern(PATTERN)))
    }

    override fun read(jsonReader: JsonReader): LocalDate {
        return LocalDate.parse(jsonReader.nextString(), DateTimeFormat.forPattern(PATTERN))
    }
}