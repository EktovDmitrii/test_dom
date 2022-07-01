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
        return promoCodesRepository.getPromoCodes().map { listPromoCodes ->
            listPromoCodes.filter { promoCode ->
                promoCode.type != SERVICE_PROMO_CODE
                if (promoCode.products.isNotEmpty()) {
                    promoCode.products.any { it.productId == productId }
                } else {
                    promoCode.products.isEmpty()
                }
            }
        }
    }
}
