package com.custom.rgs_android_dom.ui.catalog.product.service

import org.joda.time.DateTime
import java.io.Serializable

data class ServiceLauncher(
    val productId: String,
    val serviceId: String,
    val quantity: Long,
    val isPurchased: Boolean = false,
    val purchaseValidTo: DateTime? = null,
    val purchaseObjectId: String? = null
) : Serializable