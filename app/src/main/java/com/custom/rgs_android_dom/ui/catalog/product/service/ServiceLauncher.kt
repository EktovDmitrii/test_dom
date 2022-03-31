package com.custom.rgs_android_dom.ui.catalog.product.service

import org.joda.time.DateTime
import java.io.Serializable

data class ServiceLauncher(
    val productId: String,
    val serviceId: String,
    val serviceVersionId: String?,
    val quantity: Long,
    val clientProductId: String? = null,
    val isPurchased: Boolean = false,
    val purchaseValidFrom: DateTime? = null,
    val purchaseValidTo: DateTime? = null,
    val purchaseObjectId: String? = null,
    val canBeOrdered: Boolean = false
) : Serializable