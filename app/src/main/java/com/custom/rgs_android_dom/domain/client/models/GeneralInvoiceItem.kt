package com.custom.rgs_android_dom.domain.client.models

import com.custom.rgs_android_dom.utils.formatPrice
import java.io.Serializable

data class GeneralInvoiceItem(
    val amount: Int? = null,
    val itemCode: String? = null,
    val itemId: String? = null,
    val measure: String? = null,
    val name: String? = null,
    val price: Int? = null,
    val provider: GeneralInvoiceProvider? = null,
    val quantity: Int? = null,
    val vatAmount: Int? = null,
    val vatType: Int? = null
) : Serializable {

    fun getNameWithAmount(): String {
        val amountText = if (quantity != null && quantity > 1) ", $quantity шт" else ""
        return "$name$amountText"
    }

    fun getPriceText(): String {
        return if (quantity != null && quantity > 1) "x ${price?.formatPrice()}" else "${price?.formatPrice()}"
    }
}
