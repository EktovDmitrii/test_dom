package com.custom.rgs_android_dom.ui.catalog.product.single

import org.joda.time.DateTime
import java.io.Serializable

data class SingleProductLauncher(
    val productId: String,
    val productVersionId: String?,
    val serviceId: String? = null,
    val isPurchased: Boolean = false,
    val purchaseValidTo: DateTime? = null,
    val purchaseObjectId: String? = null
) : Serializable