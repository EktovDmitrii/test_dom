package com.custom.rgs_android_dom.ui.catalog.product

import org.joda.time.DateTime
import java.io.Serializable

data class ProductLauncher(
    val productId: String,
    val isPurchased: Boolean = false,
    val purchaseValidFrom: DateTime? = null,
    val purchaseValidTo: DateTime? = null,
    val purchaseObjectId: String? = null
) : Serializable