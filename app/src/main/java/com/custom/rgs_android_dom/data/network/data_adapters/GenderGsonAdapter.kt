package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.domain.client.models.Gender
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class GenderGsonAdapter : TypeAdapter<Gender>() {

    override fun write(out: JsonWriter, value: Gender) {
        val genderString = if (value == Gender.MALE) "M" else "F"
        out.value(genderString)
    }

    override fun read(jsonReader: JsonReader): Gender {
        val str = jsonReader.nextString()
        return if (str.toUpperCase(Locale.getDefault()) == "M") Gender.MALE else Gender.FEMALE
    }
}
