package com.custom.rgs_android_dom.domain.address.models

import com.google.gson.Gson
import com.yandex.mapkit.geometry.Point

data class AddressItemModel(
    var addressString: String,
    val cityFiasId: String,
    val cityName: String,
    val coordinates: Point,
    val fiasId: String,
    val geocodeId: String,
    val regionFiasId: String,
    val regionName: String
) {


    companion object {

        fun createEmpty(): AddressItemModel {
            return AddressItemModel("", "", "", Point(0.0, 0.0), "", "", "", "")
        }

        fun toString(model: AddressItemModel): String {
            val gson = Gson()
            return gson.toJson(model)
        }

        fun fromString(model: String): AddressItemModel {
            val gson = Gson()
            return gson.fromJson(model, AddressItemModel::class.java)
        }

    }

}