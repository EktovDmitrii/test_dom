package com.custom.rgs_android_dom.domain.catalog.models

import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.formatQuantity
import java.io.Serializable

data class ProductDurationModel(
    val unitType: ProductUnitType,
    val units: Int
): Serializable {

    override fun toString(): String {
        when (unitType){
            ProductUnitType.DAYS -> {
                return when{
                    units.toString().takeLast(2).toInt() in 11..19 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.daysPrefix.three")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    units.toString().takeLast(1).toInt() == 1 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.daysPrefix.one")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    units.toString().takeLast(1).toInt() in 2..4 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.daysPrefix.two")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    else -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.daysPrefix.three")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                }
             }
            ProductUnitType.MONTHS -> {
                return when{
                    units.toString().takeLast(2).toInt() in 11..19 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.monthsPrefix.three")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    units.toString().takeLast(1).toInt() == 1 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.monthsPrefix.one")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    units.toString().takeLast(1).toInt() in 2..4 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.monthsPrefix.two")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    else -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.monthsPrefix.three")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                }
            }
            ProductUnitType.YEARS -> {
                return when{
                    units.toString().takeLast(2).toInt() in 11..19 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.yearsPrefix.three")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    units.toString().takeLast(1).toInt() == 1 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.yearsPrefix.one")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                    units.toString().takeLast(1).toInt() in 2..4 -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.yearsPrefix.two")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"

                    }
                    else -> {
                        "$units ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.yearsPrefix.three")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
                    }
                }
            }
            else -> {
                return "Unknown"
            }
        }
    }

}