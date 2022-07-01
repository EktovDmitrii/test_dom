package com.custom.rgs_android_dom.domain.promo_codes

import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.repositories.PromoCodesRepository
import com.custom.rgs_android_dom.ui.constants.SERVICE_PROMO_CODE
import io.reactivex.Single

class PromoCodesInteractor(
    private val promoCodesRepository: PromoCodesRepository
) {

    fun getPromoCodes(): Single<List<PromoCodeItemModel>> {
        return promoCodesRepository.getPromoCodes()
    }

    fun activatePromoCode(promoCodeId: String): Single<PromoCodeItemModel>{
        return promoCodesRepository.activatePromoCode(promoCodeId)
    }

    fun getOrderPromoCodes(productId: String): Single<List<PromoCodeItemModel>> {
        val orderPromoCodes = promoCodesRepository.getPromoCodes().blockingGet().filter { promoCodeProduct ->
                promoCodeProduct.type != SERVICE_PROMO_CODE
            if (promoCodeProduct.products.isNotEmpty()) {
                promoCodeProduct.products.any { it.productId == productId }
            } else {
                promoCodeProduct.products.isEmpty()
            }
        }
        return Single.just(orderPromoCodes)
    }
}
