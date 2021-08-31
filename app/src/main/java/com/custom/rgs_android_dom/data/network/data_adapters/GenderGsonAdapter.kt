package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.domain.client.models.Gender
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class GenderGsonAdapter : TypeAdapter<Gender>() {

    override fun write(out: JsonWriter, value: Gender?) {
        val genderString = when (value){
            null -> {
                ""
            }
            Gender.MALE -> {
                "M"
            }
            Gender.FEMALE -> {
                "F"
            }
        }
        out.value(genderString)
    }

    override fun read(jsonReader: JsonReader): Gender? {
        val str = jsonReader.nextString()
        return when {
            str.toUpperCase(Locale.getDefault()) == "M" -> {
                Gender.MALE
            }
            str.toUpperCase(Locale.getDefault()) == "F" -> {
                Gender.FEMALE
            }
            else -> null
        }
    }
}
