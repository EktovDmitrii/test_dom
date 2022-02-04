package com.custom.rgs_android_dom.domain.purchase.model

import com.custom.rgs_android_dom.domain.catalog.models.ProductDurationModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductPriceModel
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import java.io.Serializable

//Todo расширится
data class PurchaseModel(
    val id: String,
    val defaultProduct: Boolean,
    val duration: ProductDurationModel?,
    val iconLink: String,
    val name: String,
    val price: ProductPriceModel?,
    var propertyItemModel: PropertyItemModel? = null,
    val email: String? = null,
    val agentCode: String? = null,
    val card: CardModel? = null,
    var comment: String? = null,
    val purchaseDateTimeModel: PurchaseDateTimeModel? = null
) : Serializable
