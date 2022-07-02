package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig.BUSINESS_LINE
import com.custom.rgs_android_dom.data.network.responses.PromoCodeItemResponse
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeProduct
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel

object PromoCodesMapper {

    fun responseToPromoCodesItemModels(responseItem: List<PromoCodeItemResponse>?): List<PromoCodeItemModel> {
        return responseItem?.map {

            val promoCodeProducts = it.products?.map { products ->
                PromoCodeProduct(
                    count = products.count ?: 0,
                    productId = products.productId ?: ""
                )
            }

            PromoCodeItemModel(
                businessLine = it.businessLine ?: "",
                clientId = it.clientId ?: "",
                code = it.code ?: "",
                description = it.description ?: "",
                discountInPercent = it.discountInPercent ?: 0,
                discountInRubles = it.discountInRubles ?: 0,
                expiredAt = it.expiredAt,
                id = it.id ?: "",
                name = it.name ?: "",
                productPeriodFrom = it.productPeriodFrom,
                productPeriodTo = it.productPeriodTo,
                products = promoCodeProducts ?: emptyList(),
                promoCampaignId = it.promoCampaignId ?: "",
                status = it.status ?: "",
                type = it.type ?: "",
            )
        }?.filter { it.businessLine == BUSINESS_LINE } ?: emptyList()
    }

    fun responseToPromoCodesItemModel(responseItem: PromoCodeItemResponse): PromoCodeItemModel {
        val promoCodeProducts = responseItem.products?.map { products ->
            PromoCodeProduct(
                count = products.count ?: 0,
                productId = products.productId ?: ""
            )
        }

        return PromoCodeItemModel(
            businessLine = responseItem.businessLine ?: "",
            clientId = responseItem.clientId ?: "",
            code = responseItem.code ?: "",
            description = responseItem.description ?: "",
            discountInPercent = responseItem.discountInPercent ?: 0,
            discountInRubles = responseItem.discountInRubles ?: 0,
            expiredAt = responseItem.expiredAt,
            id = responseItem.id ?: "",
            name = responseItem.name ?: "",
            productPeriodFrom = responseItem.productPeriodFrom,
            productPeriodTo = responseItem.productPeriodTo,
            products = promoCodeProducts ?: emptyList(),
            promoCampaignId = responseItem.promoCampaignId ?: "",
            status = responseItem.status ?: "",
            type = responseItem.type ?: "",
        )
    }
}
