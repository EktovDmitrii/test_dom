package com.custom.rgs_android_dom.domain.promo_codes.model

import org.joda.time.DateTime
import java.io.Serializable

data class PromoCodeItemModel(
    val businessLine: String,
    val clientId: String,
    val code: String,
    val description: String,
    val discountInPercent: Int,
    val discountInRubles: Int,
    val expiredAt: DateTime?,
    val id: String,
    val name: String,
    val productPeriodFrom: DateTime?,
    val productPeriodTo: DateTime?,
    val products: List<PromoCodeProduct>,
    val promoCampaignId: String,
    val status: String,
    val type: String
) : Serializable

data class PromoCodeProduct(
    val count: Int,
    val productId: String,
) : Serializable
