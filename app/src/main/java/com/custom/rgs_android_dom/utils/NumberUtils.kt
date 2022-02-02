package com.custom.rgs_android_dom.utils

import android.text.Html
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

fun Int.formatPrice(isFixed: Boolean = true): String {
    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val symbols: DecimalFormatSymbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = ' '
    val f = DecimalFormat("###,###", symbols)
    val formatted = f.format(this)
    if (formatted.startsWith(".")) formatted = "0".plus(formatted)
    return StringBuilder(
        if (isFixed) ""
        else "от "
    )
        .append(formatted)
        .append(' ')
        .append(Html.fromHtml("&#x20bd",0))
        .toString()
}

fun Int.formatServiceQuantity(): String {
    return when {
        this.toString().takeLast(2).toInt() in 11..19 -> "$this видов услуг"
        this.toString().takeLast(1).toInt() == 1 -> "$this вид услуг"
        this.toString().takeLast(1).toInt() in 2..4 -> "$this вида услуг"
        else -> "$this видов услуг"
    }
}