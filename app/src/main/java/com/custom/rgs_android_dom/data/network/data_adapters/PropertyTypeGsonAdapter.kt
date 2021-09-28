package com.custom.rgs_android_dom.data.network.data_adapters

import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class PropertyTypeGsonAdapter : TypeAdapter<PropertyType>() {

    override fun write(out: JsonWriter, value: PropertyType) {
        val typeString = value.type
        out.value(typeString)
    }

    override fun read(jsonReader: JsonReader): PropertyType {
        val str = jsonReader.nextString()
        return when {
            str.toLowerCase(Locale.getDefault()) == "house" -> {
                PropertyType.HOUSE
            }
            str.toLowerCase(Locale.getDefault()) == "apartment" -> {
                PropertyType.APARTMENT
            }
            else -> PropertyType.UNDEFINED
        }
    }
}
