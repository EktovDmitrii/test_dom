package com.custom.rgs_android_dom.domain.catalog.models

import java.io.Serializable

data class ProductDurationModel(
    val unitType: ProductUnitType,
    val units: Int
): Serializable {

    override fun toString(): String {
        when (unitType){
            ProductUnitType.DAYS -> {
                return when{
                    units.toString().takeLast(2).toInt() in 11..19 -> "$units дней"
                    units.toString().takeLast(1).toInt() == 1 -> "$units день"
                    units.toString().takeLast(1).toInt() in 2..4 -> "$units дня"
                    else -> "$units дней"
                } + " после покупки"
             }
            ProductUnitType.MONTHS -> {
                return when{
                    units.toString().takeLast(2).toInt() in 11..19 -> "$units месяцев"
                    units.toString().takeLast(1).toInt() == 1 -> "$units месяц"
                    units.toString().takeLast(1).toInt() in 2..4 -> "$units месяца"
                    else -> "$units месяцев"
                } + " после покупки"
            }
            ProductUnitType.YEARS -> {
                return when{
                    units.toString().takeLast(2).toInt() in 11..19 -> "$units лет"
                    units.toString().takeLast(1).toInt() == 1 -> "$units год"
                    units.toString().takeLast(1).toInt() in 2..4 -> "$units года"
                    else -> "$units лет"
                } + " после покупки"
            }
            else -> {
                return "Unknown"
            }
        }
    }

}