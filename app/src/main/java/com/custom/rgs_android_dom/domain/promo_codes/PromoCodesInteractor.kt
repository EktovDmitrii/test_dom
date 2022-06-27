package com.custom.rgs_android_dom.domain.promo_codes

import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodesItemModel
import com.custom.rgs_android_dom.domain.repositories.PromoCodesRepository
import io.reactivex.Single

class PromoCodesInteractor(
    private val promoCodesRepository: PromoCodesRepository
) {

    fun getPromoCodes(): Single<List<PromoCodesItemModel>> {
        return promoCodesRepository.getPromoCodes()
    }

    fun activatePromoCode(promoCodeId: String): Single<PromoCodesItemModel>{
        return promoCodesRepository.activatePromoCode(promoCodeId)
    }
}
