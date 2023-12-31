package com.custom.rgs_android_dom.utils

import android.text.Html
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

fun Int.formatPrice(isFixed: Boolean = true): String {
    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val symbols: DecimalFormatSymbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = ' '

    val f = DecimalFormat("###,###", symbols)
    var formatted = f.format(this.toFloat() / 100f).replace(",00", "")
    if (formatted.contains(",") && formatted.endsWith("0")){
        formatted = formatted.replaceRange(formatted.lastIndexOf("0"), formatted.length, "")
    }
    if (formatted.startsWith(",")) formatted = "0".plus(formatted)
    return StringBuilder(
        if (isFixed) ""
        else "от "
    )
        .append(formatted)
        .append(' ')
        .append(Html.fromHtml("&#x20bd",0))
        .toString()
}

fun Int.simplePriceFormat() : String {
    return StringBuilder()
        .append(this)
        .append(' ')
        .append(Html.fromHtml("&#x20bd",0))
        .toString()
}

fun Int.formatPriceGroupedByThousands(): String {
    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val symbols: DecimalFormatSymbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = ' '
    formatter.decimalFormatSymbols = symbols
    val formatted = formatter.format(this)
    return StringBuilder()
        .append(formatted)
        .append(' ')
        .append(Html.fromHtml("&#x20bd",0))
        .toString()
}



fun Int.formatQuantity(): String {
    return when {
        this.toString().takeLast(2).toInt() in 11..19 -> {
            "$this ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.plurar")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
        }
        this.toString().takeLast(1).toInt() == 1 -> {
            "$this ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.single")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
        }
        this.toString().takeLast(1).toInt() in 2..4 -> {
            "$this ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.from")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
        }
        else -> {
            "$this ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.plurar")} ${TranslationInteractor.getTranslation("app.support_service.prefix_titles.countServiceKindText.service_title")}"
        }
    }
}