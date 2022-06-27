package com.custom.rgs_android_dom.domain.promo_codes.model

import org.joda.time.DateTime

data class PromoCodesItemModel(
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
    val products: List<PromoCodeProducts>,
    val promoCampaignId: String,
    val status: String,
    val type: String
)

data class PromoCodeProducts(
    val count: Int,
    val productId: String,
)
