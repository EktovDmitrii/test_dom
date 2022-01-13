package com.custom.rgs_android_dom.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object DigitsFormatter {

    private val formatterSymbols by lazy {
        DecimalFormatSymbols().apply {
            groupingSeparator = ' '
        }
    }

    private val formatter by lazy {
        DecimalFormat().apply {
            decimalFormatSymbols = formatterSymbols
            groupingSize = 3
            maximumFractionDigits = 2
        }
    }

    fun priceFormat(price: Int): String {
        return formatter.format(price) + " ₽"
    }
}