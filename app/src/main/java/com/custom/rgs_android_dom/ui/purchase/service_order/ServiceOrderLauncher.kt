package com.custom.rgs_android_dom.ui.purchase.service_order

import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.models.DeliveryType
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import java.io.Serializable

data class ServiceOrderLauncher(
    val serviceId: String,
    val productId: String,
    val serviceVersionId: String?,
    val clientProductId: String,
    val deliveryType: DeliveryType? = null,
    val property: PropertyItemModel? = null,
    val dateTime: PurchaseDateTimeModel? = null
) : Serializable