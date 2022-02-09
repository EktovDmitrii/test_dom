package com.custom.rgs_android_dom.ui.catalog.product.single

import org.joda.time.DateTime
import java.io.Serializable

data class SingleProductLauncher(
    val productId: String,
    val serviceId: String? = null,
    val isIncluded: Boolean = false,
    val isPurchased: Boolean = false,
    val paidDate: DateTime? = null,
    val purchaseValidTo: DateTime? = null,
    val purchaseObjectId: String? = null
) : Serializable