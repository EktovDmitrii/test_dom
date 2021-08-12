package com.custom.rgs_android_dom.ui.countries.model

import android.util.Log
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import java.io.Serializable
import java.util.*

data class CountryPresentationModel(
    val id: Long,
    val name: String,
    val image: Int,
    val letterCode: String,
    val numberCode: String,
    val mask: String,
    val isSelected: Boolean
) {

    companion object {
        fun from(model: CountryModel, letterCode: String): CountryPresentationModel {
            return CountryPresentationModel(
                id = model.id,
                name = model.name,
                image = model.image,
                letterCode = model.letterCode,
                numberCode = model.numberCode,
                mask = model.mask,
                isSelected = model.letterCode == letterCode
            )
        }

        fun toCountryModel(model: CountryPresentationModel): CountryModel {
            return CountryModel(
                id = model.id,
                name = model.name,
                image = model.image,
                letterCode = model.letterCode,
                numberCode = model.numberCode,
                mask = model.mask
            )
        }
    }

}