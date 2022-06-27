package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodesItemModel
import io.reactivex.Single

interface PromoCodesRepository {

    fun getPromoCodes(): Single<List<PromoCodesItemModel>>

    fun activatePromoCode(promoCodeId: String): Single<PromoCodesItemModel>

}