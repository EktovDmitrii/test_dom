package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.PromoCodeItemResponse
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeProducts
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodesItemModel

object PromoCodesMapper {

    fun responseToPromoCodesItemModels(responseItem: List<PromoCodeItemResponse>?): List<PromoCodesItemModel> {
        return responseItem?.map {

            val promoCodeProducts = it.products?.map { products ->
                PromoCodeProducts(
                    count = products.count ?: 0,
                    productId = products.productId ?: ""
                )
            }

            PromoCodesItemModel(
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
        } ?: emptyList()
    }

    fun responseToPromoCodesItemModel(responseItem: PromoCodeItemResponse): PromoCodesItemModel {
        val promoCodeProducts = responseItem.products?.map { products ->
            PromoCodeProducts(
                count = products.count ?: 0,
                productId = products.productId ?: ""
            )
        }

        return PromoCodesItemModel(
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
