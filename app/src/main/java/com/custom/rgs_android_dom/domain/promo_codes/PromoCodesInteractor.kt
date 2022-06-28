package com.custom.rgs_android_dom.domain.promo_codes

import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.repositories.PromoCodesRepository
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
}
